package Model;

import Controller.GameController;

import java.io.Serializable;
import java.util.Locale;

/**
 * The class or object that contains the literal state of the game. All created objects, lists, cards players logs,
 * as well as logic to maniupulate the state of the game is here. Sort of the core of the game

 */
public class Gamestate implements Serializable {
	private static final long serialVersionUID = 1L;
	private int diceResult;
	private Player player1 = new Player(1);
	private Player player2 = new Player(2);
	private Deck cardDeck = new Deck();
	private Deck discard = new Deck();
	private int currentPlayerIndex =1;
	private Settings properties;
	private boolean goalScored = false;
	private GameController controller;
	
	private Dice dice = new Dice();
	private BoardState board = new BoardState("0");





//Connection/DB methods-----------------------------------------------------------------



	//------------------------------------------------------------------------
	public void setController(GameController controller) {
		this.controller = controller;
	}

	public void initializeNewGame(){
		properties = new Settings();
		properties.setLocale(Locale.ENGLISH);

		board.setPosition("A");
		currentPlayerIndex =0;

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
		player2.setLastScored();


	}



	public void playCard(Card card) {
		System.out.println("[Gamestate] playCard() called. currentPlayerIndex=" + currentPlayerIndex + " card=" + card.getCardType());
		String type= card.getCardType();
		if(currentPlayerIndex==1) {
			System.out.println("[Gamestate] -> delegating to player1.playCard()");
			player1.playCard(card);
		} else if(currentPlayerIndex==2) {
			System.out.println("[Gamestate] -> delegating to player2.playCard()");
			player2.playCard(card);
		} else {
			System.out.println("[Gamestate] -> currentPlayerIndex not 1/2: " + currentPlayerIndex);
		}
		discard.addToDeck(card);
		changeBoard(type);
		// print scores after changeBoard
		System.out.println("[Gamestate] after changeBoard: p1=" + player1.getScore() + " p2=" + player2.getScore() + " currentPlayerIndex=" + currentPlayerIndex + " goalScored=" + goalScored);
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
				System.out.println("[Gamestate.changeBoard] scoring for player2 (case P) — type=" + type + " position=" + currentPosition);
				player2.scoreUp();
				player2.setLastScored();
				player1.clearLastScored();
				goalScored = true;
				currentPlayerIndex = 0;
				board.setPosition("0");
				controller.refreshDiscard();
				controller.refreshBoard();
				break;

			case "M":
				System.out.println("[Gamestate.changeBoard] scoring for player1 (case M) — type=" + type + " position=" + currentPosition);
				player1.scoreUp();
				player1.setLastScored();
				player2.clearLastScored();
				goalScored = true;
				currentPlayerIndex = 0;
				board.setPosition("0");
				controller.refreshDiscard();
				controller.refreshBoard();
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
		//System.out.println(currentPlayerIndex);
		
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

	public int getDiceResult() {
		return dice.getDiceResult();
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
		String cardType[]={"I","M","H","R","X","Y","Z","S"};
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

	public int getp2Score() {
		return player2.getScore();
	}

	public int getp1Score() {
		return player1.getScore();
	}
	public boolean isLastScored(int player){
		if (player == 1){
			return player1.isLastScored();
		} else if (player == 2) {
			return player2.isLastScored();
		}
		return false;
	}
	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
	}

	public void setBoard(String type) {
		board.setPosition(type);
	}
	public boolean getGoalScored() {
		return goalScored;
	}

	public void clearGoalScored() {
		goalScored = false;
	}

}
