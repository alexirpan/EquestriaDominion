package framework;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import cardData.CopperCard;

/**
 * Represents a Dominion card
 * @author alexI
 *
 */
public class DomCard implements Cloneable,Comparable<DomCard>{
	//various defining characteristics
	private String[] cardTypes;
	private String cardName;
	private int cost;
	private int defaultCost; //Holds cost so that cards know cost after Bridge/Highway played
	private int potionCost;
	private int victoryPoints;
	private boolean playDurationEffect = false;
	//Lets card know about it's current state
	private boolean inPlay;
	private boolean discarded;
	private Player owner;
	private static Color treasureColor = Color.ORANGE;
	private static Color actionColor = new Color(220,220,220);
	private static Color reactionColor = new Color(56,167,229);
	private static Color durationColor = new Color(255,144,70);
	private static Color victoryColor = new Color(145,194,122);
	private static Color curseColor = new Color(216,135,221);
	private final int DISPLAY_WIDTH;
	private final int DISPLAY_HEIGHT = 19;
	private final int NAME_X_OFFSET = 7;
	private final int NAME_Y_OFFSET = 15;
	
	//Default Dominion card is a $0 cost action called Bob that does nothing
	public DomCard()
	{
		cardTypes = new String[1];
		cardTypes[0] = "Action";
		cost = 0;
		defaultCost = cost;
		potionCost = 0;
		victoryPoints = 0;
		cardName = "Bob";
		DISPLAY_WIDTH = 24;
		owner = null;
	}
	
	/**
	 * Creates a card.
	 * The owner of a card is initially set to null. The gain method sets the owner
	 */
	public DomCard(int cost, int potionCost, int victoryPoints, String name, String ... types)
	{
		cardName = name;
		cardTypes = types;
		this.cost = cost;
		defaultCost = cost;
		this.potionCost = potionCost;
		this.victoryPoints = victoryPoints;
		DISPLAY_WIDTH = 8*cardName.length();
		owner = null;
	}
	
	/**
	 * Decreases cost by n. Cost does not go below 0
	 * @param n How much the cost decreases by.
	 */
	public void decreaseCost(int n)
	{
		if (cost < n)
		{
			cost = 0;
		}
		else
		{
			cost = cost - n;
		}
	}
	
	/**
	 * Sets cost to n
	 * @param n The new cost
	 */
	public void setCost(int n)
	{
		cost = n;
	}
	
	public void setPlayDurationEffect(boolean value)
	{
		playDurationEffect = value;
	}
	
	/**
	 * Sets the owner of the card to the given player
	 * @param person The new card owner
	 */
	public void setOwner(Player person)
	{
		owner = person;
	}
	
	public Player getOwner()
	{
		return owner;
	}
	
	public String[] getTypes()
	{
		return cardTypes;
	}
	
	public void toggleInPlay()
	{
		inPlay = !inPlay;
	}
	
	public void toggleDiscarded()
	{
		discarded = !discarded;
	}
	
	public void setCardName(String newName)
	{
		cardName = newName;
	}
	
	public String getCardName()
	{
		return cardName;
	}
	
	public int getCost()
	{
		return cost;
	}
	
	public int getVP()
	{
		return victoryPoints;
	}
	
	public int getPotionCost()
	{
		return potionCost;
	}
	
	public boolean durationEffectPlayed()
	{
		return playDurationEffect;
	}
	
	/**
	 * Checks if two cards are the same. Currently does so by matching card names.
	 * @param otherCard The other card
	 * @return Whether the two cards are equal
	 */
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o.getClass() != getClass())
			return false;
		
		return cardName.equals(((DomCard) o).getCardName());
	}
	
	public void draw(int xOffset, int yOffset, Graphics g)
	{
		if (isType("Duration"))
		{
			g.setColor(durationColor);
		}
		else if (isType("Reaction"))
		{
			g.setColor(reactionColor);
		}
		else if (isType("Curse"))
		{
			g.setColor(curseColor);
		}
		else if (isType("Victory"))
		{
			g.setColor(victoryColor);
		}
		else if (isType("Action"))
		{
			g.setColor(actionColor);
		}
		else if (isType("Treasure"))
		{
			g.setColor(treasureColor);
		}
		
		//character length is about 8 pixels
		g.fillRect(xOffset, yOffset, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		g.setColor(Color.BLACK);
		g.drawString(cardName, NAME_X_OFFSET + xOffset, NAME_Y_OFFSET + yOffset);
	}
	
	public boolean isType(String type)
	{
		for (int i = 0; i < cardTypes.length; i++)
		{
			if (cardTypes[i].equals(type))
			{
				return true;
			}
		}
		return false;
	}
	
	public String toString()
	{
		return ("Name " + cardName + " Cost " + cost + " Types " + cardTypes.toString());
	}
	
	/**
	 * This method is overwritten by each card.
	 */
	public void play()
	{ 
		/*
		int index = owner.inHand(this);
		if (index != -1)
		{
			inPlay = true;
			owner.moveCardFromZoneToZone(this, owner.getHand(), owner.getPlay());
			System.out.println("You play a " + cardName);
		} 
		*/
	}
	
	public void playNextTurn()
	{
		
	}
	
	public DomCard clone()
	{
		return new DomCard(cost,potionCost,victoryPoints,cardName,cardTypes);
	}

	public int compareTo(DomCard arg0) 
	{
		//default cost, then name
		if (cost != arg0.getCost())
		{
			return cost - arg0.getCost();
		}
		else
		{
			if (potionCost != arg0.getPotionCost())
			{
				return potionCost - arg0.getPotionCost();
			}
			else
			{
				return cardName.compareTo(arg0.getCardName());
			}
		}
	}
	
	/**
	 * A method to overwrite for variable VP cards. This method should generally call setVP.
	 */
	public void calculateVP()
	{
		
	}

	public void setVP(int newValue)
	{
		victoryPoints = newValue;
	}
	
	/**
	 * A method to overwrite for reactions
	 */
	public void playReactionEffect()
	{
		
	}

	/**
	 * This method is called whenever a card would be discarded outside of Cleanup
	 * Overwrite this method for cards that trigger on discard (like Tunnel)
	 */
	public void discard()
	{
		
	}
	
	/**
	 * This method is called to determine whether the player can buy this card.
	 * Some cards will need to overwrite this method (like Grand Market), 
	 * but for the majority of cases this will work fine.
	 */
	public boolean isBuyable(SupplyCardComponent scc)
	{
		if (scc.getDisplayedPlayer() == scc.getCurrentPlayer())
		{
			if (scc.getSupply() > 0)
			{
				if (scc.getCurrentPlayer().getBuys() > 0)
				{
					if (scc.getCurrentPlayer().getMoney() >= getCost())
					{
						return true;
					}
					else
						return false;
				}
				else
					return false;
			}
			else
				return false;
		}
		else 
			return false;
	}
	
	public static String findCountsOfContainedCards(ArrayList<DomCard> lst)
	{
		if (lst.size() == 0)
			return "";
		
		ArrayList<DomCard> cards = new ArrayList<DomCard>();
		ArrayList<Integer> counts = new ArrayList<Integer>();
		DomCard card;
		int index;
		
		for (int i = 0; i < lst.size(); i++)
		{
			card = lst.get(i);
			
			if (!cards.contains(card))
			{
				cards.add(card);
				counts.add(1);
			}
			else
			{
				index = cards.indexOf(card);
				counts.set(index, counts.get(index) + 1);
			}
		}
		
		String result = "";

		for (int i = 0; i < cards.size(); i++)
		{
			result += counts.get(i) + " " + cards.get(i).getCardName() + ", ";
		}
		
		//remove the last space and comma
		return result.substring(0, result.length() - 2);
	}
	
	//temporary for now
	public int hashCode()
	{
		return cardName.hashCode() * defaultCost;
	}
	
	
}