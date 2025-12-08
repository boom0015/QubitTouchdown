package Model;

import java.io.Serializable;

/**
 * Very simple, just a dice that contains a number that represents the face up
 *
 */
public class Dice implements Serializable {
    private static final long serialVersionUID = 1L;
    private int diceResult;

    public int rollDice(){
        diceResult = (int) (Math.random() * 2);
        return diceResult;
    }
    public int getDiceResult() {
        return diceResult;
    }
}
