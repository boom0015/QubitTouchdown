package View;

import Controller.GameController;
import Net.QTouchClient;
import javax.swing.*;
import java.awt.*;
import java.util.Locale;

/**
 *Login screen that appears for connecting to the server. Serves as the entry into the game
 */
public class Login {
    private String user;
    private JFrame frame;
    private QTouchClient client;
    private int currentGameId = -1;

    public Login() {
        // No longer needs server parameter - we'll connect to remote server
    }

    public void viewLoginScreen(QTouchInterface view) {

        frame = new JFrame("QTouch Client - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());

        JLabel logoImage = new JLabel(new ImageIcon("images/logo.jpg"));
        JPanel logo = new JPanel();
        logo.add(logoImage);
        frame.add(logo, BorderLayout.NORTH);

        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // Changed labels to reflect client connecting to server
        JLabel hostLabel = new JLabel("Server Host:");
        JTextField hostField = new JTextField("localhost", 10);

        JLabel portLabel = new JLabel("Port:");
        JTextField portField = new JTextField("8888", 6);

        JButton connectButton = new JButton("Connect");
        JButton createGameButton = new JButton("Create Game");
        JButton joinGameButton = new JButton("Join Game");
        JButton showUsersButton = new JButton("Show Users");
        JButton endButton = new JButton("Exit");

        controlBar.add(hostLabel);
        controlBar.add(hostField);
        controlBar.add(portLabel);
        controlBar.add(portField);
        controlBar.add(connectButton);
        controlBar.add(createGameButton);
        controlBar.add(joinGameButton);
        controlBar.add(showUsersButton);
        controlBar.add(endButton);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.add(controlBar);

        frame.add(centerWrapper, BorderLayout.CENTER);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.append("Please connect to server and login to continue.\n");
        textArea.append("Current Valid users for testing: \n");
        textArea.append("username: asd, Pass: asd\n");
        textArea.append("Username: qwer, Pass qwer\n");

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        frame.add(scrollPane, BorderLayout.SOUTH);

        // Initially disable game buttons until connected
        createGameButton.setEnabled(false);
        joinGameButton.setEnabled(false);
        showUsersButton.setEnabled(false);

        // CONNECT BUTTON - establishes connection and authenticates
        connectButton.addActionListener(e -> {
            String host = hostField.getText().trim();
            String portText = portField.getText().trim();

            if (host.isEmpty() || portText.isEmpty()) {
                textArea.append("Error: Please enter host and port.\n");
                return;
            }

            // Get login credentials
            String[] userfields = showLoginDialog();
            if (userfields[0].isEmpty() || userfields[1].isEmpty()) {
                textArea.append("Login cancelled.\n");
                return;
            }

            int port;
            try {
                port = Integer.parseInt(portText);
            } catch (NumberFormatException ex) {
                textArea.append("Port must be a number.\n");
                return;
            }

            // Connect to server
            textArea.append("Connecting to server at " + host + ":" + port + "...\n");

            try {
                client = new QTouchClient(host, port);
                textArea.append("Connected to server!\n");

                // Authenticate
                textArea.append("Authenticating as " + userfields[0] + "...\n");
                boolean authSuccess = client.auth(userfields[0], userfields[1]);

                if (authSuccess) {
                    textArea.append("Login successful! Welcome " + userfields[0] + "!\n");
                    user = userfields[0];

                    // *** SET UP LISTENER IMMEDIATELY AFTER AUTH ***
                    textArea.append("Setting up game listener...\n");
                    setupGameListener(view, textArea);
                    textArea.append("Game listener ready.\n");

                    // Enable game buttons
                    createGameButton.setEnabled(true);
                    joinGameButton.setEnabled(true);
                    showUsersButton.setEnabled(true);
                    connectButton.setEnabled(false);

                } else {
                    textArea.append("Authentication failed. Invalid credentials.\n");

                    // Offer to add user
                    boolean addUser = promptAddUser(userfields[0]);
                    if (addUser) {
                        textArea.append("Requesting to add user " + userfields[0] + "...\n");
                        try {
                            // Send request to server to add user
                            boolean added = addUserToServer(userfields[0], userfields[1]);
                            if (added) {
                                textArea.append("User added successfully! Please connect again.\n");
                            } else {
                                textArea.append("Failed to add user to server.\n");
                            }
                        } catch (Exception addEx) {
                            textArea.append("Error adding user: " + addEx.getMessage() + "\n");
                        }
                    }

                    client.close();
                    client = null;
                }

            } catch (Exception ex) {
                textArea.append("Connection error: " + ex.getMessage() + "\n");
                ex.printStackTrace();
                client = null;
            }
        });

        // CREATE GAME BUTTON - creates a new game
        createGameButton.addActionListener(e -> {
            if (client == null) {
                textArea.append("Not connected to server.\n");
                return;
            }

            try {
                int gameId = client.createGame();
                currentGameId = gameId;
                textArea.append("Game created! Game ID: " + gameId + "\n");
                textArea.append("Waiting for another player to join...\n");
                // Listener already set up during connection

            } catch (Exception ex) {
                textArea.append("Error creating game: " + ex.getMessage() + "\n");
            }
        });

        // JOIN GAME BUTTON - joins an existing game
        joinGameButton.addActionListener(e -> {
            if (client == null) {
                textArea.append("Not connected to server.\n");
                return;
            }

            try {
                // List available games
                int[] games = client.listGames();
                if (games.length == 0) {
                    textArea.append("No games available. Create one first!\n");
                    return;
                }

                textArea.append("Available games: ");
                for (int gid : games) {
                    textArea.append(gid + " ");
                }
                textArea.append("\n");

                String gameIdStr = JOptionPane.showInputDialog(
                        frame,
                        "Enter Game ID to join:",
                        "Join Game",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (gameIdStr != null && !gameIdStr.trim().isEmpty()) {
                    int gameId = Integer.parseInt(gameIdStr);
                    boolean success = client.joinGame(gameId);

                    if (success) {
                        currentGameId = gameId;
                        textArea.append("Joined game " + gameId + "! Game starting...\n");
                        // Listener already set up during connection

                    } else {
                        textArea.append("Failed to join game " + gameId + "\n");
                    }
                }

            } catch (Exception ex) {
                textArea.append("Error joining game: " + ex.getMessage() + "\n");
            }
        });

        // SHOW USERS BUTTON - view database users
        showUsersButton.addActionListener(e -> {
            if (client == null) {
                textArea.append("Not connected to server.\n");
                return;
            }

            try {
                textArea.append("Fetching users from server...\n");
                String usersList = client.listUsers();

                if (usersList.isEmpty()) {
                    textArea.append("No users in database.\n");
                } else {
                    textArea.append("=== Users in Database ===\n");
                    String[] users = usersList.split(",");
                    for (String u : users) {
                        textArea.append(u + "\n");
                    }
                    textArea.append("========================\n");
                }
            } catch (Exception ex) {
                textArea.append("Error fetching users: " + ex.getMessage() + "\n");
            }
        });

        // END BUTTON
        endButton.addActionListener(e -> {
            textArea.append("Exiting...\n");
            frame.dispose();

            if (client != null) {
                try {
                    client.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            System.exit(0);
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Set up listener for game state updates from server
     */
    private void setupGameListener(QTouchInterface view, JTextArea textArea) {
        client.setUpdateListener(new QTouchClient.GamestateUpdateListener() {
            @Override
            public void onGamestateUpdate(Model.Gamestate newState) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        textArea.append("*** GAMESTATE UPDATE RECEIVED ***\n");
                        textArea.append("Game state updated!\n");

                        // Ensure GUI exists before updating
                        if (!view.isVisible()) {
                            textArea.append("Opening game interface...\n");

                            // Initialize controller if needed
                            if (view.getController() == null) {
                                textArea.append("Creating controller for network mode...\n");
                                GameController networkController = new GameController();
                                networkController.updateGamestate(newState);
                                view.setController(networkController);
                            }

                            view.createAndShowGUI(Locale.ENGLISH);
                            textArea.append("GUI created successfully!\n");
                            frame.setVisible(false);
                        } else {
                            textArea.append("GUI already open, updating...\n");
                        }

                        textArea.append("Updating view from gamestate...\n");
                        view.updateFromGamestate(newState);
                        textArea.append("View updated successfully!\n");
                    } catch (Exception e) {
                        textArea.append("ERROR in onGamestateUpdate: " + e.getMessage() + "\n");
                        e.printStackTrace();
                    }
                });
            }
            @Override
            public void onPlayerTurn(int playerIndex) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        textArea.append("*** PLAYER TURN: " + playerIndex + " ***\n");
                        view.showTurnIndicator(playerIndex);
                    } catch (Exception e) {
                        textArea.append("ERROR in onPlayerTurn: " + e.getMessage() + "\n");
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onScoresUpdate(int p1Score, int p2Score) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        textArea.append("*** SCORES: P1=" + p1Score + " P2=" + p2Score + " ***\n");
                        view.updateScores(p1Score, p2Score);
                    } catch (Exception e) {
                        textArea.append("ERROR in onScoresUpdate: " + e.getMessage() + "\n");
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onMessage(String message) {
                SwingUtilities.invokeLater(() -> {
                    textArea.append("Server: " + message + "\n");
                });
            }
        });
    }

    public String[] showLoginDialog() {
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Login",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        String username = "";
        String password = "";
        if (result == JOptionPane.OK_OPTION) {
            username = userField.getText();
            password = new String(passField.getPassword());
            System.out.println("Username: " + username);
        }
        String[] userfields = {username, password};
        return userfields;
    }

    /**
     * Prompt user if they want to add a new user
     */
    public boolean promptAddUser(String username) {
        int result = JOptionPane.showConfirmDialog(
                null,
                "User not found in database.\n" + "Do you want to add user: " + username + "?",
                "Add User",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Request server to add a new user
     */
    private boolean addUserToServer(String username, String password) throws Exception {
        if (client == null || !client.isConnected()) {
            // Need to reconnect temporarily to add user
            String host = "localhost";
            int port = 8888;

            try (QTouchClient tempClient = new QTouchClient(host, port)) {
                return tempClient.addUser(username, password);
            }
        }
        return false;
    }

    public QTouchClient getClient() {
        return client;
    }

    public String getUsername() {
        return user;
    }

    public int getCurrentGameId() {
        return currentGameId;
    }
}