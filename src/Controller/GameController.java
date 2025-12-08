package Controller;

import Model.*;
import View.QTouchInterface;
import View.SouthPanel;

import javax.swing.*;
import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for MVC model. Determines appropriate actions to take based on buttons pressed in view
 */
public class GameController {
    private Gamestate model;
    private QTouchInterface view;
    public GameController() {
    }
    public GameController(Gamestate model, QTouchInterface view) {
        this.model = model;
        this.view = view;
    }
    public Settings getSettings() {
        Settings settings = model.getSettings();
        return settings;
    }

    public ResourceBundle getBundle() {
        Settings settings = getSettings();
        ResourceBundle bundle = ResourceBundle.getBundle("LanguageResources.Messages");
        return bundle;
    }

    public void setLocaleAndRelaunch(Locale locale) {
        model.getSettings().setLocale(locale);

        if (view != null) view.relaunchGUI(locale);
    }

    public int getDiceResult() {
        return model.getDiceResult();

    }
    public Dice getDice() {
        return model.getDice();
    }
    public Deck getDiscard(){
        Deck discard = model.getDiscard();
        return discard;
    }

    public PlayerLog getp1Log() {
        PlayerLog p1Log = model.getP1Log();
        return p1Log;
    }

    public PlayerLog getp2Log() {
        PlayerLog p2Log = model.getP2Log();
        return p2Log;
    }

    public Deck getDraw() {
        return model.getDraw();
    }

    public void playCard(Card card) {
        System.out.println("[Controller] playCard() start: currentIndex=" + model.getIndex());
        model.playCard(card);

        // perform the model changes
        model.playCard(card);

        // attempt to draw only if a player is active (1 or 2)
        model.drawCard();

        // if a goal was scored, do NOT advance the player index;
        // changeBoard already set currentPlayerIndex = 0 in that case.
        // If no goal, advance to next player.
        if (!model.getGoalScored()) {
            model.nextPlayer();
        } else {
            // clear the flag so subsequent plays behave normally
            model.clearGoalScored(); // add this method to Gamestate (see below)
        }

        // centralize UI updates here (controller coordinates view + south panel)
        view.refreshBoard();

        refreshDiscard();         // calls view.getSouthPanel().refreshDiscard()
        System.out.println("[Controller] after model.playCard: p1=" + model.getp1Score() + " p2=" + model.getp2Score() + " goalScored=" + model.getGoalScored());
        refreshPlayer();

    }

    private void refreshPlayer() {
        view.refreshPlayer();
    }

    private void endTurn() {

    }

    public Player getPlayer(int player){
        if (player ==1){
            return model.getPlayer1();
        }else if(player==2){
            return model.getPlayer2();
        }
        else{
            return null;
        }
    }

    public BoardState getBoard() {
        return model.getBoard();

    }
    public void refreshDiscard(){
        SouthPanel southPanel = view.getSouthPanel();
        southPanel.refreshDiscard();

    }

    public int getCurrentIndex() {
       return model.getIndex();
    }

    public void loadGame() throws IOException {
        String filename = JOptionPane.showInputDialog(
                null,                         // parent component (null = center on screen)
                "Enter the name of the game file to load",           // message
                "File prompt",               // title
                JOptionPane.QUESTION_MESSAGE  // icon type
        );
        if (filename != null) {
            //use filename
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois =new ObjectInputStream(fis);
            try {
                model=(Gamestate) ois.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ois.close();
            view.relaunchGUI(null);
        }

    }

    public void saveGame() throws IOException{
        String filename = JOptionPane.showInputDialog(
                null,                         // parent component (null = center on screen)
                "Enter the name of the game file",           // message
                "File prompt",               // title
                JOptionPane.QUESTION_MESSAGE  // icon type
        );
        if (filename != null) {
            //use filename
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(model);
            oos.close();
        }
    }

    public int getp1Score() {
        return model.getp1Score();
    }
    public int getp2Score() {
        return model.getp2Score();
    }
    public void setPlayerIndex(int index){
        model.setCurrentPlayerIndex(index);
    }

    public void setBoard(String type) {
        model.setBoard(type);
    }

    public void refreshBoard() {
        view.refreshBoard();
    }

    public void cardButtonPress(Player player, Card card) {

        if (player.getIndex() == getCurrentIndex()
                && !"Pile2".equals(card.getCardType())) {
            playCard(card);
            view.refreshHand();
            refreshDiscard();
        }
    }
    /**
     * Update the gamestate reference (used when receiving updates from server)
     */
    public void updateGamestate(Model.Gamestate newGamestate) {
        this.model = newGamestate;
        // Reconnect the controller reference
        this.model.setController(this);
    }
}



