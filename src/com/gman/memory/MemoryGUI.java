package com.gman.memory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public class MemoryGUI {
	JFrame frame;							  //Frame
	Card[] cards = new Card[12];              //All cards
	JLabel label = new JLabel();			  //Label at top
	JPanel[] rows = new JPanel[3];		      //Storage row by row
	JPanel container = new JPanel();	      //Storage for buttons
	ImageIcon[] imgs = new ImageIcon[12];     //Icons for buttons
	int selectedIndex = -1;					  //Current selected index (-1 if none)
	int numOfSelected = 0;					  //Store number selected in order to end game
	boolean finishedCleanup = true;
	
	public MemoryGUI() {
		//Holds the cards in a 4 x 4 manner
		frame = new JFrame("Memory");
		//frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		
		/******* Create and Consolidate Index Array *******/
		int[] index1 = IntStream.rangeClosed(0, 5).toArray();
		int[] index2 = IntStream.rangeClosed(0, 5).toArray();
		//Consolidate into one array of values
		int[] indexes = new int[index1.length + index2.length];
		System.arraycopy(index1, 0, indexes, 0, index1.length);
		System.arraycopy(index2, 0, indexes, index1.length, index2.length);
		
		/******* Shuffle Indexes *******/
		Random rand = new Random();
		for (int i = indexes.length - 1; i > 0; i--) {
			int pointer = rand.nextInt(i + 1);
			int temp = indexes[pointer];
			indexes[pointer] = indexes[i];
			indexes[i] = temp;
		}
		
		/******* Assign Cards with Their Index *******/
		for (int i = 0; i < cards.length; i++) {
			cards[i] = new Card(indexes[i]);
		}
		
		
		/******* Action Listeners *******/
		for (int i = 0; i < cards.length; i++) {
			final int finalI = i;         //For action listener
			cards[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//If none selected, select
					if (selectedIndex == -1) {
						selectedIndex = finalI;
						cards[finalI].toggle();
					} else {
						//Sleep so user can see 2nd card
						//Use Timer to still let GUI update
						SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {

							@Override
							protected Void doInBackground() throws Exception {
								//Don't let same card be selected
								if (finalI != selectedIndex && finishedCleanup) {
									cards[finalI].toggle();
									finishedCleanup = false;
								}
								repaint();
								Thread.sleep(500);
								return null;
							}
							
							//Check for match when done
							@Override
							protected void done() {
								//Check for match
								//Don't let same card be selected
								if (finalI != selectedIndex) {
									if (cards[selectedIndex].image == cards[finalI].image) {
										//We have a match
										cards[selectedIndex].setEnabled(false);
										cards[finalI].setEnabled(false);
										selectedIndex = -1;
										numOfSelected++;
										finishedCleanup = true;
										if (numOfSelected == 6) {
											//Update label because you won
											label.setText("You Win!");
											label.setAlignmentX(Component.CENTER_ALIGNMENT);
											repaint();
										}
									} else {
										cards[selectedIndex].off();
										cards[finalI].off();
										selectedIndex = -1;
										finishedCleanup = true;
									}
								}
							}
							
						};
						//Run our timer
						worker.execute();
						
					}
				}
				
			});
		}
		
		
		//initialize panels
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new JPanel();
		}
		//Add cards to rows
		for (int i = 0; i < cards.length; i++) {
			//Based on card number
			if (i < 4) {
				rows[0].add(cards[i]);
			} else if (i < 8) {
				rows[1].add(cards[i]);
			} else if (i < 12) {
				rows[2].add(cards[i]);
			}
		}
		
		//Add label & panels to container
		label = new JLabel();
		label.setFont(label.getFont().deriveFont(64.0f));
		label.setText("Pokémon Matching");
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.add(label);
		for (int i = 0; i < rows.length; i++) {
			container.add(rows[i]);
		}
		frame.add(container, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	//Update JFrame
	protected void repaint() {
		frame.repaint();
		frame.revalidate();
	}
	
	public static void main(String[] args) {
		MemoryGUI gui = new MemoryGUI();
	}
}