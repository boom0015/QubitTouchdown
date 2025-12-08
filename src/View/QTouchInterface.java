package View;

import Controller.GameController;
import Model.BoardState;
import Model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Serves as the main game interface/View of the MVC, has action listeners which tell the controller what has happened
 * in the game
 */
public class QTouchInterface {
    private ResourceBundle bundle;
    private GameController controller;

    private JFrame frame;

    private String english = "English";
    private String french = "French";
    private String lGame = "Game";
    private String lSettings = "Settings";
    private String lAppearance = "Appearance";
    private String lHelp = "Help";
    private String lLanguage = "Language";
    private String lload = "Load";
    private String lSave = "Save";

    //Objects that will make up the GUI

    private BoardState board;
    private Board boardPanel;
    private Player player1;
    private Player player2;
    private SouthPanel southPanel;
    private JMenuBar menu;
    private PlayerPanel playerPanel1;
    private PlayerPanel playerPanel2;


    public QTouchInterface() {}
    public QTouchInterface(GameController controller) {
        this.controller = controller;
        this.bundle = controller.getBundle();

    }


    public void createAndShowGUI(Locale locale) {
        Locale currentLocale = locale;
        if (locale == null) {
            currentLocale = Locale.ENGLISH;
        }

        if (frame != null) {
            frame.dispose();
            frame = null;
        }

        frame = new JFrame("Qubit Touchdown");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLayout(new BorderLayout());

        // FIX: Load bundle directly if controller doesn't have it
        if (controller != null) {
            bundle = controller.getBundle();
        }

        if (bundle == null) {
            // Load bundle directly when controller is not initialized
            bundle = ResourceBundle.getBundle("LanguageResources.Messages", currentLocale);
        }
        english = bundle.getString("language.english");
        french = bundle.getString("language.french");
        lGame = bundle.getString("menu.game");
        lSettings = bundle.getString("menu.settings");
        lAppearance = bundle.getString("menu.appearance");
        lHelp = bundle.getString("menu.help");
        lLanguage = bundle.getString("menu.language");
        lload = bundle.getString("menu.load");
        lSave = bundle.getString("menu.save");


        menu = createMenu();

        frame.setJMenuBar(menu);
        frame.setSize(1280, 720);
        frame.setLayout(new BorderLayout());


        frame.add(createSouthPanel(), BorderLayout.SOUTH);

        player1 = controller.getPlayer(1);
        player2 = controller.getPlayer(2);
        frame.add(buildPlayerPanel(player1), BorderLayout .WEST);
        frame.add(buildPlayerPanel(player2), BorderLayout .EAST);

        boardPanel = buildBoardPanel();
        frame.add(boardPanel, BorderLayout.CENTER);



        frame.setVisible(true);
    }
    private JMenuBar createMenu(){
        Menu menu = new Menu(controller);
        JMenuBar menuBar = new JMenuBar();
        menuBar =menu.createMenu(english, french, lGame, lSettings, lAppearance, lHelp, lLanguage, lload, lSave);
        return menuBar;
    }

    public void relaunchGUI(Locale locale) {

        // Launch a new GUI with the selected language
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI(locale));
    }
    private JPanel createSouthPanel(){
        //ublic SouthPanel(GameController controller, Dice dice, Deck discard, Deck draw, PlayerLog p1Log, PlayerLog p2Log) {

       southPanel = new SouthPanel(controller, controller.getDice(), controller.getDiscard(),controller.getDraw(), controller.getp1Log(), controller.getp2Log() );
        //southPanel = new JPanel();
        //southPanel = southPanelBuilder.createSouthPanel();

        return southPanel;
    }
    private PlayerPanel buildPlayerPanel(Player player){
        if(player == player1){
            playerPanel1 = new PlayerPanel(player, controller);
            return playerPanel1;
        }
        else if(player == player2){
            playerPanel2 = new PlayerPanel(player, controller);
            return playerPanel2;
        }


        return null;
    }
    private Board buildBoardPanel(){
      board = controller.getBoard();
      Board boardPanel = new Board(board);
      return boardPanel;
    }
    public void setController(GameController controller){
        this.controller = controller;
    }
    public SouthPanel getSouthPanel(){
        return southPanel;
    }
    public void refreshBoard(){
        boardPanel.refreshBoard();
    }

    public void refreshPlayer() {
        playerPanel1.refreshHand();
        playerPanel2.refreshHand();
    }

    public void refreshHand() {
        playerPanel1.refreshHand();
        playerPanel2.refreshHand();
    }
    /**
     * Check if the interface window is visible (for network mode)
     */
    public boolean isVisible() {
        return frame != null && frame.isVisible();
    }

    /**
     * Update the entire UI from a new gamestate (for network mode)
     * When server sends updated state, this refreshes everything
     */
    public void updateFromGamestate(Model.Gamestate gs) {
        // Update the controller's model reference
        if (controller != null) {
            controller.updateGamestate(gs);
        }

        // Only refresh if components exist
        if (boardPanel != null) {
            refreshBoard();
        }
        if (playerPanel1 != null && playerPanel2 != null) {
            refreshHand();
        }
        if (southPanel != null) {
            southPanel.refreshDiscard();
        }
    }
    public GameController getController() {
        return controller;
    }
    /**
     * Show which player's turn it is (for network mode)
     */
    public void showTurnIndicator(int playerIndex) {
        // Simple implementation - just show in title bar
        if (frame != null) {
            if (playerIndex == 0) {
                frame.setTitle("Qubit Touchdown - Waiting for dice roll");
            } else {
                frame.setTitle("Qubit Touchdown - Player " + playerIndex + "'s Turn");
            }
        }

        // Optional: You could also highlight the player panel
        // or show a more prominent indicator if you want
    }

    /**
     * Update score displays (for network mode)
     */
    public void updateScores(int p1Score, int p2Score) {
        // The scores are shown in SouthPanel, so refresh it
        if (southPanel != null) {
            southPanel.refreshDiscard(); // This already updates scores
        }
    }

}
