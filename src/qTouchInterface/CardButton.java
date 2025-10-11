package qTouchInterface;

import javax.swing.*;

public class CardButton extends JButton {
	private final String cardType; //Make just a letter eg "H,I,M

	public CardButton(String cardType) {
		super(new ImageIcon("images/card" + cardType +".jpg")); // load image based on type
		this.cardType = cardType;
	}

	public String getCardType() {
		return cardType;
	}
}
