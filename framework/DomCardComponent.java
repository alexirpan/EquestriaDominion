package framework;
import java.awt.Color; 
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import javax.swing.JComponent;

import cardData.CopperCard;
import cardData.GoldCard;
import cardData.SilverCard;


public class DomCardComponent extends JComponent implements MouseListener, MouseMotionListener
{
	private DomCard theCard;
	private final int DISPLAY_WIDTH;
	private final int DISPLAY_HEIGHT = 19;
	private final int NAME_X_OFFSET = 7;
	private final int NAME_Y_OFFSET = 15;
	private static Color treasureColor = Color.ORANGE;
	private static Color actionColor = new Color(220,220,220);
	private static Color reactionColor = new Color(56,167,229);
	private static Color durationColor = new Color(255,144,70);
	private static Color victoryColor = new Color(145,194,122);
	private static Color curseColor = new Color(216,135,221);
	private boolean mouseOver = false;
	
	public DomCardComponent(DomCard givenCard)
	{
		theCard = givenCard;
		//some fiddliness happening in the display width here
		DISPLAY_WIDTH = 8 * theCard.getCardName().length() + NAME_X_OFFSET;
		initializeComponent();
	}
	
	public void initializeComponent() {
		addMouseListener(this);
		addMouseMotionListener(this);
		repaint();
	}
	
	public int getDisplayLength()
	{
		return DISPLAY_WIDTH;
	}
	
	public void paintComponent(Graphics g)
	{
		if (theCard.isType("Duration"))
		{
			g.setColor(durationColor);
		}
		else if (theCard.isType("Reaction"))
		{
			g.setColor(reactionColor);
		}
		else if (theCard.isType("Action"))
		{
			g.setColor(actionColor);
		}
		else if (theCard.isType("Treasure"))
		{
			g.setColor(treasureColor);
		}
		else if (theCard.isType("Victory"))
		{
			g.setColor(victoryColor);
		}
		else if (theCard.isType("Curse"))
		{
			g.setColor(curseColor);
		}
		g.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		if (mouseOver)
		{
			g.setColor(Color.RED);
		}
		else
		{
			g.setColor(Color.BLACK);
		}
		g.drawString(theCard.getCardName(), NAME_X_OFFSET, NAME_Y_OFFSET);
	}
	
	public DomCard getCard()
	{
		return theCard;
	}
	
	public void mouseClicked(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		if (theCard.getOwner().isThisPlayersTurn())
		{
			if (theCard.isType("Action"))
			{
				if (theCard.getOwner().getPhase().equals("Action"))
				{
					if (theCard.getOwner().getActions() > 0)
					{
						doPlayEvents(theCard);
						theCard.play();
						TextLogger.increaseIndent(-1);
						theCard.getOwner().getInterface().repaint();
					}
				}
			}
			else if (!theCard.getOwner().hasBoughtCardThisTurn())
			{
				if (theCard.isType("Treasure"))
				{
					doPlayEvents(theCard);
					theCard.play();
					TextLogger.increaseIndent(-1);
					theCard.getOwner().getInterface().repaint();
				}
			}
		}
	}
	
	/**
	 * This method does all the superficial things that happen when a card gets played
	 * 
	 * This moves the card to the play zone, changes phase if Treasure is played
	 */
	public static void doPlayEvents(DomCard card)
	{
		card.getOwner().moveCardFromZoneToZone(card, card.getOwner().getHand(), card.getOwner().getPlay());
		TextLogger.record(card.getOwner().getName() + " plays a " + card.getCardName());
		TextLogger.increaseIndent(1);
		
		if ((card.isType("Action")) && (!card.durationEffectPlayed()))
		{
			card.getOwner().addActions(-1);
		}
		
		if (card.isType("Attack"))
		{
			card.getOwner().letOpponentsRevealReactions();
		}
		
		if (card.isType("Treasure"))
		{
			card.getOwner().setPhase("Buy");
		}
	}
	
	/**
	 * This method is called when the user wants to play all the treasure at the same time. 
	 * This makes the log much shorter
	 * @param treasureCards
	 */
	public static void doPlayAllTreasureEvents(ArrayList<DomCard> treasureCards)
	{
		DomCard card = null;;
		
		if (treasureCards.size() > 0)
		{
			String s = DomCard.findCountsOfContainedCards(treasureCards);
			
			for (int i = 0; i < treasureCards.size(); i++)
			{
				card = treasureCards.get(i);
				card.getOwner().moveCardFromZoneToZone(card, card.getOwner().getHand(), card.getOwner().getPlay());
			}
			
			//below only called when card != null
			card.getOwner().setPhase("Buy");
			TextLogger.record(card.getOwner().getName() + " plays " + s + ".");
		}
	}
	
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseMoved(MouseEvent arg0) 
	{
		
	}
}

