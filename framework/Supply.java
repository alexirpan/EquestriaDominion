package framework;
import java.awt.Graphics; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JComponent;

import cardData.*;

/**
 * Represents the supply, holds all the buttons as well.
 * @author 95007461
 *
 */
public class Supply extends JComponent
{
	private ArrayList<SupplyCardComponent> cards = new ArrayList<SupplyCardComponent>(); //List of all supply cards
	private int numberOfEmptyPiles = 0;
	private int numberOfPlayers;
	private final int BASIC_X_OFFSET = 200;
	private final int BASIC_Y_OFFSET = 0;
	
	/**
	 * Creates a Supply with the given Kingdom cards. Assumes 2 players
	 * @param cardsToUse An ArrayList of SupplyCards to use
	 */
	public Supply(ArrayList<DomCard> cardsToUse)
	{
		this(cardsToUse, 2);
	}
	
	/**
	 * Creates a Supply with the given cards and the given number of players
	 * This assumes that there are 7 basic cards in the game.
	 * @param cardsToUse An ArrayList of SupplyCards to use
	 * @param players The number of players in the game
	 */
	public Supply(ArrayList<DomCard> cardsToUse, int players)
	{
		for (int i = 0; i < 10; i++)
		{
			cards.add(new SupplyCardComponent(cardsToUse.get(i), 0, 64*i));
		}
		
		for (int i = 10; i < cardsToUse.size(); i++)
		{
			cards.add(new SupplyCardComponent(cardsToUse.get(i), 
					BASIC_X_OFFSET, 
					BASIC_Y_OFFSET + 64*(i - 10)));
		}
		numberOfPlayers = players;
		initializeComponent();
	}
	
	private void initializeComponent() 
	{
		//modify size for non-basic victory cards in supply
		for (int i = 0; i < cards.size() - 7; i++)
		{
			if (cards.get(i).getCard().isType("Victory"))
			{
				cards.get(i).setSupply(numberOfPlayers * 4);
			}
		}
		
		//modify basic cards
		for (int i = cards.size() - 7; i < cards.size(); i++)
		{
			SupplyCardComponent supplyCard = cards.get(i);
			
			if (supplyCard.getCard().equals(new EstateCard()))
			{
				supplyCard.setSupply(numberOfPlayers * 7);
			}
			else if (supplyCard.getCard().isType("Victory"))
			{
				supplyCard.setSupply(numberOfPlayers * 4);
			}
			else if (supplyCard.getCard().equals(new GoldCard()))
			{
				supplyCard.setSupply(30);
			}
			else if (supplyCard.getCard().equals(new SilverCard()))
			{
				supplyCard.setSupply(40);
			}
			else if (supplyCard.getCard().equals(new CopperCard()))
			{
				supplyCard.setSupply(60);
			}
			else if (supplyCard.getCard().equals(new CurseCard()))
			{
				supplyCard.setSupply(10*(numberOfPlayers - 1));
			}
		}
		
		for (int i = 0; i < cards.size(); i++)
		{
			add(cards.get(i));
		}
		
	}

	public int getNumberEmptyPiles()
	{
		calcNumberEmptyPiles();
		return numberOfEmptyPiles;
	}
	
	public void calcNumberEmptyPiles()
	{
		numberOfEmptyPiles = 0;
		for (int i = 0; i < cards.size(); i++)
		{
			if (((SupplyCardComponent) cards.get(i)).empty())
			{
				numberOfEmptyPiles++;
			}
		}
	}
	
	/**
	 * Sets the current player for every card in supply
	 * @param person The player who is to gain the card
	 */
	public void setCurrentPlayer(Player person)
	{
		for (int i = 0; i < cards.size(); i++)
		{
			cards.get(i).setCurrentPlayer(person);
		}
	}
	
	public void setDisplayedPlayer(Player person)
	{
		for (int i = 0; i < cards.size(); i++)
		{
			cards.get(i).setDisplayedPlayer(person);
		}
	}
	
	/**
	 * Makes the various buy buttons appear or disappear
	 *
	 */
	public void setBuyableCards()
	{
		for (int i = 0; i < cards.size(); i++)
		{
			cards.get(i).calcCanBuy();
		}
	}
	
	public int getSupplyOfCard(DomCard card)
	{
		for (int i = 0; i < cards.size(); i++)
		{
			if (cards.get(i).getCard().equals(card))
			{
				return cards.get(i).getSupply();
			}
		}
		return -1;
	}
	
	/*
	*
	 * Has the current player gain the card with the given name. If the card is not in supply, nothing happens.
	 * This method is called when a card needs to be gained without player choice (i.e. a Curse giver)
	 * This method assumes that a card does not appear in the supply twice.
	 * @param name The name of the card to gain
	 
	public void gainCard(Player person, String name)
	{
		for (int i = 0; i < cards.size(); i++)
		{
			if (cards.get(i).getCard().getCardName().equals(name))
			{
				cards.get(i).gain(person);
			}
		}
	}
	*/
	
	public ArrayList<SupplyCardComponent> getCards()
	{
		return cards;
	}
	
	/**
	 * Does events that happen after a player gains a card.
	 * Right now, will only decrease the supply
	 * @param card The card that was gain
	 */
	public void doGainEvents(DomCard card)
	{
		for (int i = 0; i < cards.size(); i++)
		{
			if (cards.get(i).getCard().equals(card))
			{
				cards.get(i).setSupply(cards.get(i).getSupply() - 1);
			}
		}
	}
	
	public int lookUpSupply(DomCard card)
	{
		for (int i = 0; i < cards.size(); i++)
		{
			if (cards.get(i).getCard().equals(card))
			{
				return cards.get(i).getSupply();
			}
		}
		
		return -1;
	}
	
	public void resetText()
	{
		for (int i = 0; i < cards.size(); i++)
		{
			cards.get(i).resetText();
		}
	}
}
