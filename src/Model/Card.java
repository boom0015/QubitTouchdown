package Model;

import java.io.Serializable;

/**
 * Represents the individual cards comprising the deck. Mostly just used for representation and typing logic
 */
public class Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cardType;
    private String imgPath = "images/card"+cardType+".jpg";

    public Card(String type) {
        this.cardType = type;
        imgPath = "images/card"+cardType+".jpg";
    }
    public String getCardType() {
        return cardType;
    }

    public String getImgPath() {
        return imgPath;
    }
}
