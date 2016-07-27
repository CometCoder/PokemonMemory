package com.gman.memory;

import java.awt.Dimension;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Card extends JButton{
	public ImageIcon img;
	public int image;
	public boolean isSelected = false;

	public Card(int image) {
		this.image = image;
		this.setPreferredSize(new Dimension(256, 256));
		try {
			img = new ImageIcon(ImageIO.read(getClass().getResource("assets/" + image + ".jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Toggle if button is selected
	public void toggle() {
		isSelected = !isSelected;
		if (isSelected) {
			this.setIcon(img);
		} else {
			this.setIcon(null);
		}
	}
	
	//For resetting game, just change image without changing into a new card
	public void newImage(int image) {
		this.image = image;
		try {
			img = new ImageIcon(ImageIO.read(getClass().getResource("assets/" + image + ".jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Toggle off
	public void off() {
		isSelected = false;
		this.setIcon(null);
	}
}