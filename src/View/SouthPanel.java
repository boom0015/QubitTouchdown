package View;

import Controller.GameController;
import Model.Deck;
import Model.Dice;
import Model.PlayerLog;

import javax.swing.*;
import java.awt.*;

/**
 * The Player logs, the card pile, and the discard/deck are contained in the south panel. This class collects yhose
 * elements into one panel for the central interface
 */
public class SouthPanel extends JPanel {
    private GameController controller;
    private  JTextArea eastLog;
    private  JTextArea westLog;
    private Dice dice;
    private Deck deck;
    private Deck discard;

    private PlayerLog p1Log;
    private PlayerLog p2Log;

    private JPanel discardPanel;
    private JButton discardPile;

public SouthPanel(GameController controller, Dice dice, Deck discard, Deck draw, PlayerLog p1Log, PlayerLog p2Log) {
    this.controller = controller;
    this.dice = dice;
    this.discard = discard;
    this.deck = draw;
    this.p1Log = p1Log;
    this.p2Log = p2Log;

    setLayout(new BorderLayout());

    eastLog = new JTextArea(10, 20);
    eastLog.setEditable(false);
    add(new JScrollPane(eastLog), BorderLayout.EAST);

    westLog = new JTextArea(10, 20);
    westLog.setEditable(false);
    add(new JScrollPane(westLog), BorderLayout.WEST);

    JPanel centerPiles = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 0));

    JPanel drawPanel = new JPanel(new BorderLayout(5, 5));
    drawPanel.add(new JLabel("Draw", SwingConstants.CENTER), BorderLayout.NORTH);
    JButton drawPile = new JButton(new ImageIcon("images/pile1.jpg"));
    drawPile.setPreferredSize(new Dimension(100, 150));
    drawPanel.add(drawPile, BorderLayout.CENTER);

    discardPanel = new JPanel(new BorderLayout(5, 5));
    discardPanel.add(new JLabel("Discard", SwingConstants.CENTER), BorderLayout.NORTH);

    discardPile = new JButton(new ImageIcon(discard.getTopCard().getImgPath()));
    discardPile.setPreferredSize(new Dimension(100, 150));
    discardPanel.add(discardPile, BorderLayout.CENTER);

    JPanel dicePanel = new JPanel(new BorderLayout(5, 5));
    JLabel diceLabel = new JLabel("0", SwingConstants.CENTER);
    diceLabel.setFont(diceLabel.getFont().deriveFont(48f)); // big number
    JButton rollDice = new JButton("Roll Dice");
    dicePanel.add(diceLabel, BorderLayout.CENTER);
    dicePanel.add(rollDice, BorderLayout.SOUTH);

    rollDice.addActionListener(e -> diceLabel.setText(String.valueOf(dice.rollDice())));

    centerPiles.add(drawPanel);
    centerPiles.add(dicePanel, BorderLayout.CENTER);
    centerPiles.add(discardPanel);

    add(centerPiles);
}

public JPanel createSouthPanel() {
    JPanel southPanel = new JPanel();
    southPanel.setLayout(new BorderLayout());

    eastLog = new JTextArea(10, 20);
    eastLog.setEditable(false);
    southPanel.add(new JScrollPane(eastLog), BorderLayout.EAST);

    westLog = new JTextArea(10, 20);
    westLog.setEditable(false);
    southPanel.add(new JScrollPane(westLog), BorderLayout.WEST);

    JPanel centerPiles = new JPanel(new FlowLayout(FlowLayout.CENTER,100 , 0));

    JPanel drawPanel = new JPanel(new BorderLayout(5, 5));
    drawPanel.add(new JLabel("Draw", SwingConstants.CENTER), BorderLayout.NORTH);
    JButton drawPile = new JButton(new ImageIcon("images/pile1.jpg"));
    drawPile.setPreferredSize(new Dimension(100, 150));
    drawPanel.add(drawPile, BorderLayout.CENTER);


    discardPanel = new JPanel(new BorderLayout(5,5));
    discardPanel.add(new JLabel("Discard", SwingConstants.CENTER), BorderLayout.NORTH);


    discardPile = new JButton(new ImageIcon(discard.getTopCard().getImgPath()));
    discardPile.setPreferredSize(new Dimension(100, 150));
    discardPanel.add(discardPile, BorderLayout.CENTER);

    JPanel dicePanel = new JPanel(new BorderLayout(5, 5));
    JLabel diceLabel = new JLabel("0", SwingConstants.CENTER);
    diceLabel.setFont(diceLabel.getFont().deriveFont(48f)); // big number
    JButton rollDice = new JButton("Roll Dice");
    dicePanel.add(diceLabel, BorderLayout.CENTER);
    dicePanel.add(rollDice, BorderLayout.SOUTH);

    rollDice.addActionListener(e -> diceLabel.setText(String.valueOf(dice.rollDice())));


    centerPiles.add(drawPanel);
    centerPiles.add(dicePanel, BorderLayout.CENTER);
    centerPiles.add(discardPanel);

    southPanel.add(centerPiles);

    return southPanel;
}

public void refreshDiscard(){
    String newImgPath = discard.getTopCard().getImgPath();
    discardPile.setIcon(new ImageIcon(newImgPath));
    discardPanel.revalidate();
    discardPanel.repaint();
}

private void refreshEastLog(){
    eastLog.revalidate();
    eastLog.repaint();
}
private void refreshWestLog(){
    westLog.revalidate();
    westLog.repaint();
}

}

