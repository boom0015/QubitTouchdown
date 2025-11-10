package Model;

import java.util.Locale;

/**
 * The class that will hold the various settings and the logic to manipulate the game including saving and loading the game
 *  changing language and whatever else we want it to do.
 */
public class Settings {
    private Locale language;


    public void setLocale(Locale locale) {
        this.language = locale;
    }
}
