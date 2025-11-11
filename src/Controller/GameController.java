package Controller;

import Model.*;
import View.QTouchInterface;
import View.SouthPanel;

import javax.swing.*;
import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class I'm nott quite sure how to implement just yet. Actions recieved by the view are sent here to essentially
 * be preocessed and determine the relevanyt logic to implement in the model.
 */
public class GameController {
    private Gamestate model;
    private QTouchInterface view;

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
        ResourceBundle bundle = ResourceBundle.getBundle("LanguageResources.messages");
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
        model.playCard(card);
        model.drawCard();
        model.nextPlayer();

        view.refreshBoard();
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

}


