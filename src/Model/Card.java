package Model;

/**
 * Represents the individual cards comprising the deck. Mostly just used for representation and typing logic
 */
public class Card {
    private String cardType;
    private String imgPath = "images/card"+cardType+".png";

    public Card(String type) {
        this.cardType = type;
    }
    public String getCardType() {
        return cardType;
    }
}
