package View;

import Controller.GameController;
import Model.Card;
import Model.Player;
import qTouchInterface.CardButton;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel that is composed of the player and their hand.
 */
public class PlayerPanel extends JPanel {
    private Player player;
    private GameController controller;

    private JPanel playerHandPanel;

    public PlayerPanel(Player player, GameController controller) {
        this.player = player;
        this.controller = controller;
    }
    public JPanel playerBuilder(Player player){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Dropdown
        String[] players = {"Player 1", "Player 2"};
        JComboBox<String> playerSelect = new JComboBox<>(players);
        //set height not width so it stretches
        playerSelect.setMaximumSize(new Dimension(Integer.MAX_VALUE, playerSelect.getPreferredSize().height));

        // Player display
        JLabel nameLabel = new JLabel("Player 1", SwingConstants.CENTER);

        JLabel imageLabel = new JLabel(new ImageIcon("images/player1.jpg"), SwingConstants.CENTER);

        // Update when selection changes
        playerSelect.addActionListener(e -> {
            String selected = (String) playerSelect.getSelectedItem();
            nameLabel.setText(selected);

            // Pick image based on player name
            String imagePath = switch (selected) {
                case "Player 2" -> "images/player2.jpg";
                default -> "images/player1.jpg";
            };

            imageLabel.setIcon(new ImageIcon(imagePath));
        });

        playerSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components
        panel.add(playerSelect, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.NORTH);
        panel.add(nameLabel, BorderLayout.NORTH);

        playerHandPanel = PlayerHand(player.getHand());
        panel.add(playerHandPanel, BorderLayout.CENTER);
        return panel;
    }
    private JPanel PlayerHand(ArrayList<Card> cards){
        JPanel panel = new JPanel(new GridLayout(2, 2, 1, 1)); // 2x2 with gaps


        for (Card card : cards) {
            String type = card.getCardType();
           CardButton cardButton = new CardButton(type);
            cardButton.setPreferredSize(new Dimension(100, 150));
            cardButton.setMaximumSize(new Dimension(100, 150));
            cardButton.setMinimumSize(new Dimension(100, 150));

            cardButton.addActionListener(e -> {
                        if (player.getIndex()== controller.getCurrentIndex()) {
                            controller.playCard(card);
                            refreshHand();
                            controller.refreshDiscard();
                        }
                        }


            );
                //Add logic to print log to player text box / game logic

            // Wrap the card in a FlowLayout panel to keep its size
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            wrapper.add(cardButton);

            panel.add(wrapper);
        }

        return panel;
    }

    private void refreshHand() {
        ArrayList<Card> cards = player.getHand();

        playerHandPanel.removeAll();

        for (Card card : cards) {
            String type = card.getCardType();
            CardButton cardButton = new CardButton(type);
            cardButton.setPreferredSize(new Dimension(100, 150));
            cardButton.setMaximumSize(new Dimension(100, 150));
            cardButton.setMinimumSize(new Dimension(100, 150));

            cardButton.addActionListener(e -> {
                        if (player.getIndex()== controller.getCurrentIndex()) {
                            controller.playCard(card);
                            refreshHand();
                            controller.refreshDiscard();
                        }
                    }

            );
            //Add logic to print log to player text box / game logic

            // Wrap the card in a FlowLayout panel to keep its size
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            wrapper.add(cardButton);

            playerHandPanel.add(wrapper);
        }

        revalidate();
        repaint();
    }


}
