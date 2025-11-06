package Model;

import java.util.ArrayList;

/**
 *Just a list of cards, this'll be used to represent the cards in the actual deck, as well as another
 * deck object for discards. Not sure if we actually need one? Maybe for game ending logic either way.
 * The deck is essentially used to determine the game's end. We can technically make it
 * bigger or smaller as the player dictates but we'll put that aside for now
 */
public class Deck {
    public ArrayList<Card> cardsInDeck;

    /**
     * Logic to build the deck. Not sure if I should implement shuffle here or make a new method for it
     * @return
     */
    public Deck CreateDeck() {
        Deck d = new Deck();
        cardsInDeck = new ArrayList<>();

        return d;
    }
    public void addToDeck(Card card) {
        cardsInDeck.add(card);
    }
    public void shuffle() {

    }
}
