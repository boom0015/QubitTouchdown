package Model;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * The player information. Used to build the players hand, their profile image, their individual log etc.
 */
public class Player implements Serializable {
    private String characterName;
    //imgpath?
    private ArrayList<Card> Hand = new ArrayList<>(4);
    private PlayerLog log;
    int playerIndex;
    int touchdowns = 0;
    boolean lastScored = false;
    private static final long serialVersionUID = 1L;

    public Player(int index){
        this.playerIndex = index;
    }

    public String getCharacterName() {
        return characterName;
    }
    /**
     * Draws the card at the top of the deck then removes it from the list
     */
    public Card drawCard(Deck inDeck) {
        if (inDeck.cardsInDeck.isEmpty()) {
            return new Card("Pile2");
        }
        Card newCard = inDeck.cardsInDeck.getFirst();
        Hand.add(newCard);
        inDeck.cardsInDeck.removeFirst();
        return newCard;
    }
    public void playCard(Card Card){
    	Hand.remove(Card);
    }
    public ArrayList<Card> getHand(){
    	return Hand;
    }
    public PlayerLog getLog() {
        return log;
    }

    public int getIndex() {
        return playerIndex;
    }

    public int getScore() {
        return touchdowns;
    }
    public boolean isLastScored() {
        return lastScored;
    }

    public void setLastScored() {
        lastScored = true;
    }
    public void scoreUp(){
        touchdowns++;
        System.out.println("[Player] scoreUp() called for playerIndex=" + playerIndex + ", newScore=" + touchdowns);
    }

    public void clearLastScored() { lastScored = false; }
}
