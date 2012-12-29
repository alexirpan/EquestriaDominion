package framework;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * Draws the deck. As in, those vertical lines that represent cards. Yeah, not a heavyweight class
 * @author alexI
 *
 */
public class DeckComponent extends JComponent 
{
	
	public void paintComponent(Graphics g)
	{
		Deck d = new Deck(10);
		d.draw(0, 0, g);
	}
	
	class Deck
	{
		private int deckSize = 0;
		
		public Deck(int numCards)
		{
			deckSize = numCards;
		}
		
		public void draw(int xoffset, int yoffset, Graphics g)
		{
			
			for (int i = 0; i < deckSize; i++)
			{
				g.setColor(Color.BLACK);
				g.drawLine(xoffset + 3*i, yoffset, xoffset + 3*i, yoffset + 8);
			}
		}
	}
	
}
