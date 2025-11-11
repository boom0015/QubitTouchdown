package View;

import Model.BoardState;

import javax.swing.*;
import java.awt.*;

/**
 * The center/board panel. Displays the current board/position
 */
public class Board extends JPanel {
    private BoardState board;
    JLabel boardImage;
    JPanel boardPanel;

    public Board(BoardState board) {
        this.board = board;

        setLayout(new BorderLayout());              // be explicit
        boardImage = new JLabel(new ImageIcon(board.getBoardImg()));
        boardPanel = new JPanel();
        boardPanel.add(boardImage);
        add(boardPanel, BorderLayout.CENTER);      // add image directly to this JPanel
    }


    public JPanel buildBoard(BoardState board) {
        boardPanel = new JPanel();
        boardImage = new JLabel(new ImageIcon(board.getBoardImg()));
        boardPanel.add(boardImage);
        return boardPanel;
    }
    public void refreshBoard(){
        String newImg = board.getBoardImg();
        //System.out.println(newImg);
        boardImage.setIcon(new ImageIcon(newImg));
        boardPanel.revalidate();
        boardPanel.repaint();
    }
}
