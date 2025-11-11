package View;

import Controller.GameController;
import Model.BoardState;
import Model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

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


        bundle = controller.getBundle();

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
    private JPanel buildPlayerPanel(Player player){
        PlayerPanel playerPanelBuilder = new PlayerPanel(player, controller);
        JPanel playerPanel =  new JPanel();
        playerPanel = playerPanelBuilder.playerBuilder(player);
        return playerPanel;
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
}
