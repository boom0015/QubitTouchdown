package Model;

import java.io.Serializable;

/**
 * Class that essentially holds the current board image being presented and the logic to change it based on the card
 * played.
 */
public class BoardState implements Serializable {
    private static final long serialVersionUID = 1L;
    public BoardState() {}
    public BoardState(String type){
        this.position = type;
        imgPath = "images/qtboard"+ position + ".jpg";
    }
    //Maybe rename to position?
    protected String position; //The current board/ position of ball eg:boardI boardP etc.
    public String imgPath = "images/qtboard"+ position + ".jpg";

    public String getBoardInUse() {
        return position;
    }
    public String getBoardImg() {
        return "images/qtboard"+ position + ".jpg";
    }
    public void setPosition(String position) { this.position = position; }
    /**
     * Implement logic to change board based on card last played or diceroll at beginning
     */


}
