package Net;

import Model.Gamestate;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Client for connecting to QTouch game server.
 * Handles both sending commands and receiving server updates.
 */
public class QTouchClient implements Closeable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;
    private Thread listenerThread;
    private GamestateUpdateListener updateListener;

    /**
     * Interface for receiving server-pushed updates
     */
    public interface GamestateUpdateListener {
        void onGamestateUpdate(Gamestate newState);
        void onPlayerTurn(int playerIndex);
        void onScoresUpdate(int p1Score, int p2Score);
        void onMessage(String message);
    }

    public QTouchClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        // Start listener thread for incoming messages
        startListener();

        // Read welcome message
        String welcome = in.readLine();
        System.out.println("Server: " + welcome);
    }

    /**
     * Start background thread to listen for server messages
     */
    private void startListener() {
        listenerThread = new Thread(() -> {
            try {
                String line;
                while (running && (line = in.readLine()) != null) {
                    System.out.println("Server -> " + line);
                    handleServerMessage(line);
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    /**
     * Handle messages pushed from server (broadcasts)
     */
    private void handleServerMessage(String message) {
        String[] parts = message.split("#", 2);
        String cmd = parts[0];

        try {
            switch (cmd) {
                case "GAMESTATE_UPDATE":
                    if (parts.length > 1 && updateListener != null) {
                        Gamestate state = deserializeGamestate(parts[1]);
                        updateListener.onGamestateUpdate(state);
                    }
                    break;

                case "PLAYER_TURN":
                    if (parts.length > 1 && updateListener != null) {
                        int playerIndex = Integer.parseInt(parts[1]);
                        updateListener.onPlayerTurn(playerIndex);
                    }
                    break;

                case "SCORES":
                    if (parts.length > 1 && updateListener != null) {
                        String[] scores = parts[1].split("#");
                        int p1 = Integer.parseInt(scores[0]);
                        int p2 = Integer.parseInt(scores[1]);
                        updateListener.onScoresUpdate(p1, p2);
                    }
                    break;

                default:
                    // Queue response for synchronous methods
                    messageQueue.offer(message);

                    // Also notify listener
                    if (updateListener != null) {
                        updateListener.onMessage(message);
                    }
            }
        } catch (Exception e) {
            System.err.println("Error handling server message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set listener for server-pushed updates
     */
    public void setUpdateListener(GamestateUpdateListener listener) {
        this.updateListener = listener;
    }

    /**
     * Send command and wait for response
     */
    private String sendAndReceive(String msg) throws IOException {
        out.println(msg);
        try {
            // Wait up to 5 seconds for response
            return messageQueue.poll(5, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new IOException("Interrupted while waiting for response");
        }
    }

    // ========== Authentication ==========

    public boolean auth(String username, String password) throws IOException {
        String resp = sendAndReceive("AUTH#" + username + "#" + password);
        return resp != null && resp.startsWith("AUTH_OK");
    }

    /**
     * Add a new user to the server database
     */
    public boolean addUser(String username, String password) throws IOException {
        String resp = sendAndReceive("ADD_USER#" + username + "#" + password);
        return resp != null && resp.startsWith("ADD_USER_OK");
    }

    /**
     * List all users in the database
     */
    public String listUsers() throws IOException {
        String resp = sendAndReceive("LIST_USERS");
        if (resp != null && resp.startsWith("LIST_USERS_OK#")) {
            return resp.substring("LIST_USERS_OK#".length());
        }
        return "";
    }

    // ========== Game Management ==========

    public int createGame() throws IOException {
        String resp = sendAndReceive("CREATE_GAME");
        if (resp != null && resp.startsWith("CREATE_GAME_OK#")) {
            return Integer.parseInt(resp.split("#")[1]);
        }
        throw new IOException("Failed to create game: " + resp);
    }

    public boolean joinGame(int gameId) throws IOException {
        String resp = sendAndReceive("JOIN_GAME#" + gameId);
        return resp != null && resp.startsWith("JOIN_GAME_OK");
    }

    public int[] listGames() throws IOException {
        String resp = sendAndReceive("LIST_GAMES");
        if (resp != null && resp.startsWith("LIST_GAMES_OK")) {
            String[] parts = resp.split("#");
            if (parts.length == 1) return new int[0]; // No games
            int[] gameIds = new int[parts.length - 1];
            for (int i = 1; i < parts.length; i++) {
                gameIds[i - 1] = Integer.parseInt(parts[i]);
            }
            return gameIds;
        }
        return new int[0];
    }

    // ========== Gameplay Actions ==========

    public boolean playCard(String cardType) throws IOException {
        String resp = sendAndReceive("PLAY_CARD#" + cardType);
        return resp != null && resp.startsWith("PLAY_OK");
    }

    public String drawCard() throws IOException {
        String resp = sendAndReceive("DRAW_CARD");
        if (resp != null && resp.startsWith("DRAW_OK#")) {
            return resp.split("#")[1];
        }
        throw new IOException("Failed to draw card: " + resp);
    }

    public int rollDice() throws IOException {
        String resp = sendAndReceive("ROLL_DICE");
        if (resp != null && resp.startsWith("ROLL_OK#")) {
            return Integer.parseInt(resp.split("#")[1]);
        }
        throw new IOException("Failed to roll dice: " + resp);
    }

    // ========== Persistence (Database operations) ==========

    public int saveGamestate(String username, String filename, Gamestate gamestate) throws Exception {
        byte[] bytes = SerializationUtil.serializeToBytes(gamestate);
        String b64 = Base64.getEncoder().encodeToString(bytes);
        String resp = sendAndReceive("SAVE#" + username + "#" + filename + "#" + b64);
        if (resp != null && resp.startsWith("SAVE_OK#")) {
            return Integer.parseInt(resp.split("#", 2)[1]);
        }
        throw new IOException("Save failed: " + resp);
    }

    public Gamestate loadGamestate(String username, int id) throws Exception {
        String resp = sendAndReceive("LOAD#" + username + "#" + id);
        if (resp != null && resp.startsWith("LOAD_OK#")) {
            String b64 = resp.substring("LOAD_OK#".length());
            byte[] bytes = Base64.getDecoder().decode(b64);
            return (Gamestate) SerializationUtil.deserializeFromBytes(bytes);
        }
        throw new IOException("Load failed: " + resp);
    }

    public String listSavedGames(String username) throws IOException {
        String resp = sendAndReceive("LIST#" + username);
        if (resp != null && resp.startsWith("LIST_OK#")) {
            return resp.substring("LIST_OK#".length());
        }
        throw new IOException("List failed: " + resp);
    }

    // ========== Utility Methods ==========

    private Gamestate deserializeGamestate(String base64) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(base64);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Gamestate) ois.readObject();
        }
    }

    @Override
    public void close() throws IOException {
        running = false;
        try {
            out.println("QUIT");
        } catch (Exception ignored) {}

        if (listenerThread != null) {
            listenerThread.interrupt();
        }

        socket.close();
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}