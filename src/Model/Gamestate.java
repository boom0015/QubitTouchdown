package Model;

import java.util.Locale;

/**
 * The class or object that contains the literal state of the game. All created objects, lists, cards players logs
 * Whatever, it all goes in here so we have a state that can be used for modelling purposes
 * as well as savestates
 */
public class Gamestate {
	private int diceResult;
	private Player player1 = new Player(1);
	private Player player2 = new Player(2);
	private Deck cardDeck = new Deck();
	private Deck discard = new Deck();
	private int currentPlayerIndex =1;
	private Settings properties;
	
	private Dice dice = new Dice();
	private BoardState board = new BoardState();

	public void initializeNewGame(){
		properties = new Settings();
		properties.setLocale(Locale.ENGLISH);

		board = new BoardState("A");
		currentPlayerIndex =1;

		cardDeck = new Deck();
		cardDeck.cardsInDeck.removeFirst();
		createDeck();


		player1 = new Player(1);
		player2 = new Player(2);
		for(int i=0;i<4;i++) {
			player1.drawCard(cardDeck);
		}
		for(int i=0;i<4;i++) {
			player2.drawCard(cardDeck);
		}


	}


	
	public void playCard(Card card) {
		String type= card.getCardType();
		if(currentPlayerIndex==1) {
			player1.playCard(card);
		}else if(currentPlayerIndex==2) {
			player2.playCard(card);
		}
		discard.addToDeck(card);
		changeBoard(type);
	}

	public Card drawCard(){
		Card drawnCard = null;
		if(currentPlayerIndex==1) {
			drawnCard=player1.drawCard(cardDeck);
		}else if(currentPlayerIndex ==2) {
			drawnCard= player2.drawCard(cardDeck);
		}
		return drawnCard;
	}

	public void changeBoard(String type) {
		String currentPosition = board.position;
		switch(currentPosition) {
			case "0":
				if(type.equals("R")) {
					board.position="J";
				}else if(type.equals("H")) {
					board.position="P";
				}else if(type.equals("X") || type.equals("Y")) {
					board.position="1";
				}
				break;
			case "1":
				if(type.equals("R")) {
					board.position="I";
				}
				else if(type.equals("H")) {
					board.position="M";
				}else if(type.equals("X") || type.equals("Y")) {
					board.position="0";
				}
				break;
			case "I":
				if(type.equals("R")) {
					board.position="0";
				}
				else if(type.equals("S")) {
					board.position="M";
				}else if(type.equals("X") || type.equals("Z") || type.equals("H")) {
					board.position="J";
				}
				break;
			case "J":
				if(type.equals("R")) {
					board.position="1";
				}
				else if(type.equals("S"))  {
					board.position="P";
				}else if(type.equals("X")|| type.equals("Z") || type.equals("H")) {
					board.position="I";
				}
				break;
			case "P":
				if(type.equals("H")) {
					board.position="0";
				}
				break;
			case "M":
				if(type.equals("H")) {
					board = new BoardState("1");
				}
				break;
			default:
				board.position="0";
		}
	}
	
	public void nextPlayer() {
		if(currentPlayerIndex ==1) {
			currentPlayerIndex=2;
		}else if(currentPlayerIndex ==2) {
			currentPlayerIndex=1;
		}
		
	}
	
	public void save() {
		
	}
	
	public void load() {
		
	}

	/**
	 * roll the dice and get the result
	 */
	public void rollDice() {
		diceResult = dice.rollDice();
	}

	public Settings getSettings() {
		return properties;
	}

	public Dice getDice() {
		return dice;
	}

	public Deck getDiscard() {
		return discard;
	}

	public PlayerLog getP1Log() {
		return player1.getLog();
	}
	public PlayerLog getP2Log() {
		return player2.getLog();
	}

	public Deck getDraw() {
		return cardDeck;
	}
	public BoardState getBoard() {
		return board;
	}

	public Player getPlayer1() {
		return player1;
	}
	public Player getPlayer2() {
		return player2;
	}
	public void createDeck() {
		String cardType[]={"I","M","H","R","X","Y","Z"};
		int cardAmount=0;
		for(int i=0;i<8;i++) {
			for(int j =0;j<cardType.length;j++) {
				cardAmount++;
				Card newCard = new Card(cardType[j]);
				cardDeck.addToDeck(newCard);
				if(cardAmount == 52) {
					i=8;
					break;
				}
			}
		}
		cardDeck.shuffle();
	}

	public int getIndex() {
		return currentPlayerIndex;
	}
}
