package Model;

/**
 * Very simple, just a dice that contains a number that represents the face up
 * Not really sure if it contains any rolling logic. Probably?
 */
public class Dice {
    private int diceResult;

    public int rollDice(){
        diceResult = (int) (Math.random() * 2);
        return diceResult;
    }
    public int getDiceResult() {
        return diceResult;
    }
}
