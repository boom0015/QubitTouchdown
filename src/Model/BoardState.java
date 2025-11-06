package Model;

/**
 * Class that essentially holds the current board image being presented and the logic to change it based on the card
 * played.
 */
public class BoardState {
    //Maybe rename to position?
    private String position; //The current board/ position of ball eg:boardI boardP etc.
    public final String imgPath = "images/qtboard"+ position + ".png";

    public String getBoardInUse() {
        return position;
    }

    /**
     * Implement logic to change board based on card last played or diceroll at beginning
     */


}
