package Model;

import java.util.ArrayList;
/**
 * The player information. Used to build the players hand, their profile image, their individual log etc.
 */
public class Player {
    private String characterName;
    //imgpath?
    private ArrayList<Card> Hand = new ArrayList<>(4);
    private PlayerLog log;
    int playerIndex;
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
}
