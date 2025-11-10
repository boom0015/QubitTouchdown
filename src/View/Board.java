package View;

import Model.BoardState;

import javax.swing.*;

/**
 * The center/board panel. Displays the current board/position
 */
public class Board {
    private BoardState board;
    JLabel boardImage;
    JPanel boardPanel;

    public Board(BoardState board) {}
    public JPanel buildBoard(BoardState board) {
        boardPanel = new JPanel();
        boardImage = new JLabel(new ImageIcon(board.getBoardImg()));
        boardPanel.add(boardImage);
        return boardPanel;
    }
    public void refreshBoard(){
        String newImg = board.getBoardImg();
        boardImage.setIcon(new ImageIcon(newImg));
        boardPanel.revalidate();
        boardPanel.repaint();
    }
}
