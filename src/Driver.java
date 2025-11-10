import View.QTouchInterface;
import Model.Gamestate;
import Controller.GameController;

import java.util.Locale;


public class Driver {
    public static void main(String[] args) {
        Gamestate model = new Gamestate();
        model.initializeNewGame();
        QTouchInterface view = new QTouchInterface();
        GameController controller = new GameController(model, view);
        view.setController(controller);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                view.createAndShowGUI(Locale.ENGLISH);
            }
        });

    }
}
