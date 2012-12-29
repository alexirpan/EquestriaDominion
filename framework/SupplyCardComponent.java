package framework;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Component represents a card in the supply
 * It's implemented similarly to DomCardComponent, but with different results when clicked
 * @author alexI
 *
 */
public class SupplyCardComponent extends JComponent {
	//various defining characteristics
	private final DomCard REPRESENTED_CARD;
	private Player currentPlayer;
	private Player displayedPlayer;
	private boolean isBuyable = false;
	private final int RECT_WIDTH;
	private final int RECT_HEIGHT = 19;
	private final int X_OFFSET;
	private final int Y_OFFSET;
	private final int COST_X_OFFSET = 0;
	private final int COST_Y_OFFSET = 10;
	private final int NAME_X_OFFSET = 7;
	private final int NAME_Y_OFFSET = 25; 
	private final int SUP_X_OFFSET = 0; 
	private final int SUP_Y_OFFSET = 44; 
	private final int BUY_X_OFFSET = SUP_X_OFFSET + 20;
	private final int BUY_Y_OFFSET = 32;
	private int numInSupply;
	private static Color treasureColor = Color.ORANGE;
	private static Color actionColor = new Color(220,220,220);
	private static Color reactionColor = new Color(56,167,229);
	private static Color durationColor = new Color(255,144,70);
	private static Color victoryColor = new Color(145,194,122);
	private static Color curseColor = new Color(216,135,221);
	private BuyButton buy;
	
	/**
	 * Creates a Supply card with the given card and 10 in the supply
	 * @param card The card
	 * @param xOffset The x offset of the component
	 * @param yOffset The y offset of the component
	 */
	public SupplyCardComponent(DomCard card, int xOffset, int yOffset)
	{
		this(card, 10, xOffset, yOffset);
	}
	
	/**
	 * Creates a Supply card.
	 * @param card The card the supply represents
	 * @param start The amount initally in supply
	 * @param xOffset The x offset of the component
	 * @param yOffset The y offset of the component
	 */
	public SupplyCardComponent(DomCard card, int start, int xOffset, int yOffset)
	{
		REPRESENTED_CARD = card;
		numInSupply = start;
		//fiddliness with Supply Cards here
		RECT_WIDTH = Math.min(8 * REPRESENTED_CARD.getCardName().length() + NAME_X_OFFSET, 197);
		X_OFFSET = xOffset;
		Y_OFFSET = yOffset;
		buy = new BuyButton();
		initializeComponent();
	}
	
	public void initializeComponent() 
	{
		setSize(X_OFFSET + RECT_WIDTH + 100, Y_OFFSET + 100); 
		buy.setBounds(X_OFFSET + BUY_X_OFFSET, Y_OFFSET + BUY_Y_OFFSET, 50, 32);
		buy.setVisible(false);
		add(buy);
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		if (REPRESENTED_CARD.isType("Duration"))
		{
			g.setColor(durationColor);
		}
		else if (REPRESENTED_CARD.isType("Reaction"))
		{
			g.setColor(reactionColor);
		}
		else if (REPRESENTED_CARD.isType("Action"))
		{
			g.setColor(actionColor);
		}
		else if (REPRESENTED_CARD.isType("Treasure"))
		{
			g.setColor(treasureColor);
		}
		else if (REPRESENTED_CARD.isType("Victory"))
		{
			g.setColor(victoryColor);
		}
		else if (REPRESENTED_CARD.isType("Curse"))
		{
			g.setColor(curseColor);
		}
		g.fillRect(X_OFFSET, Y_OFFSET + COST_Y_OFFSET + 2, RECT_WIDTH, RECT_HEIGHT);
		g.setColor(Color.BLACK);
		g.drawString(REPRESENTED_CARD.getCardName(), X_OFFSET + NAME_X_OFFSET, Y_OFFSET + NAME_Y_OFFSET);
		//Because we need to draw a string, not a number
		g.drawString("" + numInSupply, X_OFFSET + SUP_X_OFFSET, Y_OFFSET + SUP_Y_OFFSET);
		g.drawString("$" + REPRESENTED_CARD.getCost(), X_OFFSET + COST_X_OFFSET, Y_OFFSET + COST_Y_OFFSET);
		if (numInSupply == 0)
		{
			g.drawLine(X_OFFSET, Y_OFFSET + COST_Y_OFFSET + 9, X_OFFSET + RECT_WIDTH, Y_OFFSET + COST_Y_OFFSET + 9);
		}
	} 
	
	public int getSupply()
	{
		return numInSupply;
	}
	
	public void setSupply(int newAmount)
	{
		numInSupply = newAmount;
	}
	
	public void decreaseSupply()
	{
		if (numInSupply > 0)
		{
			numInSupply--;
		}
		else
		{
			TextLogger.record("Tried to remove from empty supply");
		}
	}
	
	public DomCard getCard()
	{
		return REPRESENTED_CARD;
	}
	
	/**
	 * Sets the current player to another player
	 * @param nextPlayer The next player
	 */
	public void setCurrentPlayer(Player nextPlayer)
	{
		currentPlayer = nextPlayer;
	}
	
	public void setDisplayedPlayer(Player nextPlayer)
	{
		displayedPlayer = nextPlayer;
	}
	
	/**
	 * Returns the current player
	 * @return The player supply is giving cards to
	 */
	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}
	
	/**
	 * Returns the displayed player
	 * @return The player who is currently being viewed
	 */
	public Player getDisplayedPlayer()
	{
		return displayedPlayer;
	}
	
	public boolean empty()
	{
		return (numInSupply == 0);
	}
	
	/**
	 * A button that lets you buy things!
	 * Theoretically, will only appear if the player can buy that card.
	 * @author alexI
	 */
	private class BuyButton extends JButton implements ActionListener
	{
		public BuyButton()
		{
			super("+");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) 
		{
			//questionable times to buy cards
			//1. Actions in hand with actions left during action phase
			//2. Treasures in hand at any point
			
			if (currentPlayer.isThisPlayersTurn())
			{
				if (getText().equals("?!"))
				{
					buy();
				}
				else if ((currentPlayer.searchZone(currentPlayer.getHand(), "Treasure").size() > 0) ||
						((currentPlayer.getPhase().equals("Action")) &&
								(currentPlayer.searchZone(currentPlayer.getHand(), "Action").size() > 0) &&
								(currentPlayer.getActions() > 0)))
				{
					setText("?!");
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else buy();
			}
		}
		
		/**
		 * The current player buys a copy of this card
		 */
		public void buy()
		{
			setText("+");
			if (numInSupply > 0)
			{
				currentPlayer.gainCard(REPRESENTED_CARD);
				currentPlayer.addMoney(-REPRESENTED_CARD.getCost());
				currentPlayer.addBuys(-1);
				calcCanBuy();
				TextLogger.record(currentPlayer.getName() + " buys a " + REPRESENTED_CARD.getCardName() + ".");
				doBuyEvents(currentPlayer);
				this.getTopLevelAncestor().repaint();
			}
			else
			{
				TextLogger.record("Tried to buy from empty supply.");
			}
		}
		
	}
	
	public void resetText()
	{
		buy.setText("+");
	}
	
	public static void doBuyEvents(Player person) 
	{
		person.setPhase("Buy");
		person.setHasBoughtCardThisTurn(true);
		if (person.getBuys() == 0)
		{
			person.endTurn();
		}
	}
	
	/**
	 * The specified player gains a copy of this card
	 *
	 */
	public void gain(Player person)
	{
		if (numInSupply > 0)
		{
			person.gainCard(REPRESENTED_CARD);
			this.getTopLevelAncestor().repaint();
		}
		else
		{
			TextLogger.record("Tried to gain from empty supply");
		}
	}
	
	/**
	 * Calculates if the player can buy the given card, then makes the buy button appear or disappear
	 *
	 */
	public void calcCanBuy() 
	{
		isBuyable = REPRESENTED_CARD.isBuyable(this);
		buy.setVisible(isBuyable);
		repaint();
	}
}
