package Model;

/**
 * Very simple, just a dice that contains a number that represents the face up
 * Not really sure if it contains any rolling logic. Probably?
 */
public class Dice {
    private int diceResult;

    public void rollDice(){
        diceResult = (int) (Math.random() * 6) + 1;
    }
    public int getDiceResult() {
        return diceResult;
    }
}
