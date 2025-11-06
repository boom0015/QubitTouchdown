package Model;

import java.util.ArrayList;
/**
 * The player information. Used to build the players hand, their profile image, their individual log etc.
 */
public class Player {
    private String characterName;
    //imgpath?
    private ArrayList<Card> Hand;
    private PlayerLog log;

    public String getCharacterName() {
        return characterName;
    }
    public void drawCard() {}
    public void playCard(){}
    public void getHand(){}
}
