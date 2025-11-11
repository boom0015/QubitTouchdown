package View;

import Controller.GameController;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;

/**
 * I believe the menu was already made. Simply needs to be moved here.
 */
public class Menu extends JMenuBar{
    private JMenuBar menuBar;
    private GameController controller;

    public Menu(GameController controller){
        this.controller = controller;
    }
    public JMenuBar createMenu(String english,
     String french, String lGame, String lSettings, String lAppearance, String lHelp, String lLanguage, String lload
    , String lSave) {
        menuBar = new JMenuBar();
        JMenu menu, settings, help, language, appearance;
        JMenuItem englishItem, frenchItem, load, save, exit;

        // create the menu item for game
        menu = new JMenu(lGame);
        load= new JMenuItem(lload);
        menu.add(load);

        save= new JMenuItem(lSave);
        menu.add(save);
        load.addActionListener(e -> {
            try {
                controller.loadGame();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to load game: " + ex.getMessage());
            }
        });

        save.addActionListener(e -> {
            try {
                controller.saveGame();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to save game: " + ex.getMessage());
            }
        });


        menuBar.add(menu);


        //add the settings
        settings = new JMenu(lSettings);
        menuBar.add(settings);

        //add the language to the settings
        settings.addSeparator();
        language = new JMenu(lLanguage);

        //add English and French to the language sub menu
        englishItem = new JMenuItem(english);
        language.add(englishItem);
        frenchItem = new JMenuItem(french);
        language.add(frenchItem);
        settings.add(language);

        //Action listeners for changing language, launches new window so discardds everything
        //In order to simply change language, I'll have to make every
        //label a variable that can be recalled.
        englishItem.addActionListener(e -> controller.setLocaleAndRelaunch(Locale.ENGLISH));
        frenchItem.addActionListener(e -> controller.setLocaleAndRelaunch(Locale.FRENCH));


        //add appearance
        settings.addSeparator();
        appearance = new JMenu(lAppearance);
        settings.add(appearance);

        //create the help menu
        help = new JMenu(lHelp);
        menuBar.add(help);

        return menuBar;
    }
}
