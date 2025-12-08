package qTouchInterface;

import java.awt.*;

import javax.swing.*;

import java.util.Locale;
import java.util.ResourceBundle;



public class QTouchInterface {


	private static JTextArea eastLog;
	private static JTextArea westLog;
	private int diceResult = 0;
	private static ResourceBundle bundle;

	//private variables so the language can be changed
	private String english = "English";
	private String french = "French";
	private String lGame = "Game";
	private String lSettings = "Settings";
	private String lAppearance = "Appearance";
	private String lHelp = "Help";
	private String lLanguage = "Language";

	
/*	public static void main(String[] args) {
	       javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI(Locale.ENGLISH);
	            }
	       });
	}
*/
	
	static private void createAndShowGUI(Locale locale) {
		JFrame frame = new JFrame("Qubit Touchdown");
		QTouchInterface qInterface = new QTouchInterface();
		QTouchInterface.bundle = ResourceBundle.getBundle("LanguageResources.messages", locale);
		qInterface.english = bundle.getString("language.english");
		qInterface.french = bundle.getString("language.french");
		qInterface.lGame = bundle.getString("menu.game");
		qInterface.lSettings = bundle.getString("menu.settings");
		qInterface.lAppearance = bundle.getString("menu.appearance");
		qInterface.lHelp = bundle.getString("menu.help");
		qInterface.lLanguage = bundle.getString("menu.language");



		frame.setJMenuBar(qInterface.createMenu());
	    frame.setSize(1280, 720);
		frame.setLayout(new BorderLayout());


		frame.add(qInterface.createSouthPanel(), BorderLayout.SOUTH);

		frame.add(qInterface.playerSelector(), BorderLayout .WEST);
		frame.add(qInterface.playerSelector(), BorderLayout .EAST);


		frame.add(qInterface.Board(), BorderLayout.CENTER);


		eastLog.append(bundle.getString("greeting.p1"));
		westLog.append(bundle.getString("greeting.p2"));
	    frame.setVisible(true);
	}
	private JMenuBar createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu, settings, help, language, appearance;
		JMenuItem englishItem, frenchItem;

		// create the menu item for game
		menu = new JMenu(lGame);
        menuBar.add(menu);
        
        
        //add the settings
        settings = new JMenu(lSettings);
        menuBar.add(settings);
        
        //add the language to the settings 
        settings.addSeparator();
        language = new JMenu(lLanguage);
        
        //add English and French to the language sub menu
        englishItem = new JMenuItem(english);
        language.add(englishItem);
        frenchItem = new JMenuItem(french);
        language.add(frenchItem);
        settings.add(language);

		//Action listeners for changing language, launches new window so discardds everything
		//In order to simply change language, I'll have to make every
		//label a variable that can be recalled.
		englishItem.addActionListener(e -> relaunchGUI(new Locale("en")));
		frenchItem.addActionListener(e -> relaunchGUI(new Locale("fr")));


        //add appearance
        settings.addSeparator();
        appearance = new JMenu(lAppearance);
        settings.add(appearance);
        
        //create the help menu
        help = new JMenu(lHelp);
        menuBar.add(help);
        
        return menuBar;
	}
	private void relaunchGUI(Locale locale) {
		// Find the top-level frame containing eastLog
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(eastLog);
		if (topFrame != null) {
			topFrame.dispose(); // close the old frame
		}

		// Launch a new GUI with the selected language
		javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI(locale));
	}
	private void switchLanguage(Locale locale) {
		bundle = ResourceBundle.getBundle("LanguageResources.messages", locale);

		// Update all menu text
		JMenuBar menuBar = createMenu();
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(eastLog);
		topFrame.setJMenuBar(menuBar);

		// Update other components that use text
		//updateLabels();
	}

	//Side panels
	private JPanel playerSelector(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// Dropdown
		String[] players = {"Player 1", "Player 2"};
		JComboBox<String> playerSelect = new JComboBox<>(players);
		//set height not width so it stretches
		playerSelect.setMaximumSize(new Dimension(Integer.MAX_VALUE, playerSelect.getPreferredSize().height));

		// Player display
		JLabel nameLabel = new JLabel("Player 1", SwingConstants.CENTER);

		JLabel imageLabel = new JLabel(new ImageIcon("images/player1.jpg"), SwingConstants.CENTER);

		// Update when selection changes
		playerSelect.addActionListener(e -> {
			String selected = (String) playerSelect.getSelectedItem();
			nameLabel.setText(selected);

			// Pick image based on player name
			String imagePath = switch (selected) {
				case "Player 2" -> "images/player2.jpg";
				default -> "images/player1.jpg";
			};

			imageLabel.setIcon(new ImageIcon(imagePath));
		});

		playerSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
		imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Add components
		panel.add(playerSelect, BorderLayout.NORTH);
		panel.add(imageLabel, BorderLayout.NORTH);
		panel.add(nameLabel, BorderLayout.NORTH);

		panel.add(PlayerHand(), BorderLayout.CENTER);
		return panel;
	}
	private JPanel Board(){
		JPanel panel = new JPanel();
		JLabel boardImage = new JLabel(new ImageIcon("images/board.jpg"));
		panel.add(boardImage);
		return panel;
	}
	private JPanel PlayerHand(){
		JPanel panel = new JPanel(new GridLayout(2, 2, 1, 1)); // 2x2 with gaps
		String[] cards = {"y", "x", "h", "z"};

		for (String type : cards) {
			CardButton card = new CardButton(type);
			card.setPreferredSize(new Dimension(100, 150));
			card.setMaximumSize(new Dimension(100, 150));
			card.setMinimumSize(new Dimension(100, 150));

			card.addActionListener(e -> {
				CardButton clicked = (CardButton) e.getSource();
				//Add logic to print log to player text box / game logic
			});

			// Wrap the card in a FlowLayout panel to keep its size
			JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			wrapper.add(card);

			panel.add(wrapper);
		}

		return panel;
	}
	private JPanel createSouthPanel(){
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());

		eastLog = new JTextArea(10, 20);
		eastLog.setEditable(false);
		southPanel.add(new JScrollPane(eastLog), BorderLayout.EAST);

		westLog = new JTextArea(10, 20);
		westLog.setEditable(false);
		southPanel.add(new JScrollPane(westLog), BorderLayout.WEST);

		JPanel centerPiles = new JPanel(new FlowLayout(FlowLayout.CENTER,100 , 0));

		JPanel drawPanel = new JPanel(new BorderLayout(5, 5));
		drawPanel.add(new JLabel("Draw", SwingConstants.CENTER), BorderLayout.NORTH);
		JButton drawPile = new JButton(new ImageIcon("images/pile1.jpg"));
		drawPile.setPreferredSize(new Dimension(100, 150));
		drawPanel.add(drawPile, BorderLayout.CENTER);


		JPanel discardPanel = new JPanel(new BorderLayout(5,5));
		discardPanel.add(new JLabel("Discard", SwingConstants.CENTER), BorderLayout.NORTH);

		JButton discardPile = new JButton(new ImageIcon("images/cardpile2.jpg"));
		discardPile.setPreferredSize(new Dimension(100, 150));
		discardPanel.add(discardPile, BorderLayout.CENTER);

		JPanel dicePanel = new JPanel(new BorderLayout(5, 5));
		JLabel diceLabel = new JLabel("0", SwingConstants.CENTER);
		diceLabel.setFont(diceLabel.getFont().deriveFont(48f)); // big number
		JButton rollDice = new JButton("Roll Dice");
		dicePanel.add(diceLabel, BorderLayout.CENTER);
		dicePanel.add(rollDice, BorderLayout.SOUTH);

		rollDice.addActionListener(e -> diceLabel.setText(String.valueOf(rollDice())));


		centerPiles.add(drawPanel);
		centerPiles.add(dicePanel, BorderLayout.CENTER);
		centerPiles.add(discardPanel);

		southPanel.add(centerPiles);

    	return southPanel;
	}
	private int rollDice() {
		int result = (int)(Math.random() * 2); // returns 0 or 1
		diceResult = result;
		return result;
	}

}

