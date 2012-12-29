package framework;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import cardData.ProvinceCard;

/**
 * Represents a player. Each player knows all zones that are specific to one player. 
 * Each player also knows what cards are in the supply and trash
 * @author alexI
 *
 */
public class Player 
{
	private int actions;
	private int buys;
	private int money;
	private int victoryPointTokens;
	private int friendshipTokens = 0;
	private int turns;
	private int copperValue = 1;
	private int totalVP = 3;
	private final int DIALOG_X_START = 200;
	private final int DIALOG_Y_START = 200;
	private boolean allowedToPlayOrBuy = true;
	private boolean affectedByAttacks = true;
	private boolean isThisPlayersTurn = false;
	private boolean hasBoughtCardThisTurn = false;
	private String currentPhase;
	private String name;
	private final String DISCARD_PURPOSE = "Discard";
	private final String CHOICE_PURPOSE = "Choice";
	private final String TRASH_PURPOSE = "Trash";
	private Supply supply;
	private ArrayList<DomCard> hand = new ArrayList<DomCard>();
	private ArrayList<DomCard> deck = new ArrayList<DomCard>();
	private ArrayList<DomCard> play = new ArrayList<DomCard>();
	private ArrayList<DomCard> discardPile = new ArrayList<DomCard>();
	private ArrayList<DomCard> revealedCards = new ArrayList<DomCard>();
	private ArrayList<DomCard> setAside = new ArrayList<DomCard>();
	public static ArrayList<DomCard> trash = new ArrayList<DomCard>(); //known to all players
	private Player[] allPlayers;
	PlaytesterDisplay gameInterface;
	ArrayList<String> lastChoices = new ArrayList<String>();
	
	/**
	 * Player created with default number of each parameter and default name
	 */
	public Player()
	{
		this("No Name");
	}
	
	public Player(String name)
	{
		actions = 1;
		buys = 1;
		money = 0;
		victoryPointTokens = 0;
		turns = 1;
		currentPhase = "Action";
		this.name = name;
	}
	
	//Various methods that add actions/buys/money
	public void addActions(int n)
	{
		actions = actions + n;
		if (n > 0)
		{
			if (n == 1)
			{
				TextLogger.record(name + " gets +1 action.");
			}
			else
			{
				TextLogger.record(name + " gets +" + n + " actions.");
			}
		}
	}
	
	public void addBuys(int n)
	{
		buys = buys + n;
		if (n > 0)
		{
			if (n == 1)
			{
				TextLogger.record(name + " gets +1 buy.");
			}
			else
			{
				TextLogger.record(name + " gets +" + n + " buys.");
			}
		}
	}
	
	public void addMoney(int n)
	{
		money = money + n;
		//TextLogger.record(name + " gets +$" + n + ".");
	}
	
	public void addVPTokens(int n)
	{
		victoryPointTokens = victoryPointTokens + n;
		TextLogger.record(name + " gets +" + n + " VP.");
	}
	public void addFriendship(int n)
	{
		friendshipTokens += n;
		TextLogger.record(name + " gets +" + n + " friendship.");
	}
	
	public void increaseCopperValue(int n)
	{
		copperValue += n;
		TextLogger.record("Copper is worth $" + copperValue + " for " + name + ".");
	}
	
	/**
	 * Pass this method the array of all players. Other methods will account for only dealing with the opponents.
	 * Should be changed later, as now getting opponents is not recommended
	 * @param players An array of all players
	 */
	public void setPlayers(Player[] players)
	{
		allPlayers = players;
	}
	
	public void opponentsDrawCards(int n)
	{
		for (int i = 0; i < n; i++)
		{
			opponentsDrawCard();
		}
		
		if (n == 1)
		{
			TextLogger.record("Each opponent draws 1 card.");
		}
		else
		{
			TextLogger.record("Each opponent draws " + n + " cards.");
		}
	}
	
	public void setIsThisPlayersTurn(boolean b)
	{
		isThisPlayersTurn = b;
	}
	
	public boolean isThisPlayersTurn()
	{
		return isThisPlayersTurn;
	}
	
	public void opponentsDrawCard()
	{
		for (int i = 0; i < allPlayers.length; i++)
		{
			if (!allPlayers[i].equals(this))
			{
				allPlayers[i].drawCard();
			}
		}
	}
	
	public void opponentsGetFriendship(int n)
	{
		for (int i = 0; i < allPlayers.length; i++)
		{
			if (!allPlayers[i].equals(this))
			{
				allPlayers[i].addFriendship(n);
			}
		}
	}
	
	public void opponentsGainCard(DomCard card)
	{
		for (int i = 0; i < allPlayers.length; i++)
		{
			if (!allPlayers[i].equals(this))
			{
				allPlayers[i].gainCard(card);
			}
		}
	}
	
	/**
	 * Returns all players
	 */
	public Player[] getPlayers()
	{
		return allPlayers;
	}
	
	public ArrayList<Player> getOpponentsAffectedByAttacks()
	{
		ArrayList<Player> p = new ArrayList<Player>();
		
		for (int i = 0; i < allPlayers.length; i++)
		{
			if (!allPlayers[i].equals(this) && 
					(allPlayers[i].isAffectedByAttacks()))
			{
				p.add(allPlayers[i]);
			}
		}
		
		return p;
	}
	
	public void setHasBoughtCardThisTurn(boolean b)
	{
		hasBoughtCardThisTurn = b;
	}
	
	public boolean hasBoughtCardThisTurn()
	{
		return hasBoughtCardThisTurn;
	}
	
	/**
	 * Tells the player about the game interface
	 */
	public void setGameInterface(PlaytesterDisplay ui)
	{
		gameInterface = ui;
	}
	
	/**
	 * Searches the specified zone for cards based on the given parameter
	 * @param zone The zone to search.
	 * @param parameter A parameter. A card type searches for all cards of that type. Otherwise, it searches for all cards of that name
	 * @return A list of the cards that satisfy the parameter
	 */
	public ArrayList<DomCard> searchZone(ArrayList<DomCard> zone, String parameter)
	{
		ArrayList<DomCard> result = new ArrayList<DomCard>();
		//Stupid switch statement won't work on Strings :(
		
		if (parameter.equals("Action"))
		{
			for (int i = 0; i < zone.size(); i++)
			{
				if (zone.get(i).isType("Action"))
				{
					result.add(zone.get(i));
				}
			}
			return result;
		}
		else if (parameter.equals("Victory"))
		{
			for (int i = 0; i < zone.size(); i++)
			{
				if (zone.get(i).isType("Victory"))
				{
					result.add(zone.get(i));
				}
			}
			return result;
		}
		else if (parameter.equals("Treasure"))
		{
			for (int i = 0; i < zone.size(); i++)
			{
				if (zone.get(i).isType("Treasure"))
				{
					result.add(zone.get(i));
				}
			}
			return result;
		}
		else if (parameter.equals("Duration"))
		{
			for (int i = 0; i < zone.size(); i++)
			{
				if (zone.get(i).isType("Duration"))
				{
					result.add(zone.get(i));
				}
			}
			return result;
		}
		else if (parameter.equals("Reaction"))
		{
			for (int i = 0; i < zone.size(); i++)
			{
				if (zone.get(i).isType("Reaction"))
				{
					result.add(zone.get(i));
				}
			}
			return result;
		}
		else if (parameter.equals("Curse"))
		{
			for (int i = 0; i < zone.size(); i++)
			{
				if (zone.get(i).isType("Curse"))
				{
					result.add(zone.get(i));
				}
			}
			return result;
		}
		else
		{
			for (int i = 0; i < zone.size(); i++)
			{
				if (zone.get(i).getCardName().equals(parameter))
				{
					result.add(zone.get(i));
				}
			}
			return result;
		}
	}
	
	/**
	 * Searches the discard pile for certain cards, based on a parameter 
	 * 
	 * Basic parameters: Action, Victory, Treasure, Duration, Reaction, Curse parameters search for all cards with that type
	 * Any other parameter will search for all cards with that name
	 * 
	 * @param parameter The parameter to keep in mind while searching
	 * @return A list of cards that satisfy the condition
	 */
	public ArrayList<DomCard> searchDiscardPile(String parameter)
	{
		ArrayList<DomCard> result = new ArrayList<DomCard>();
		//Stupid switch statement won't work on Strings :(
		
		if (parameter.equals("Action"))
		{
			for (int i = 0; i < discardPile.size(); i++)
			{
				if (discardPile.get(i).isType("Action"))
				{
					result.add(discardPile.get(i));
				}
			}
			return result;
		}
		else if (parameter.equals("Victory"))
		{
			for (int i = 0; i < discardPile.size(); i++)
			{
				if (discardPile.get(i).isType("Victory"))
				{
					result.add(discardPile.get(i));
				}
			}
			return result;
		}
		else if (parameter.equals("Treasure"))
		{
			for (int i = 0; i < discardPile.size(); i++)
			{
				if (discardPile.get(i).isType("Treasure"))
				{
					result.add(discardPile.get(i));
				}
			}
			return result;
		}
		else if (parameter.equals("Duration"))
		{
			for (int i = 0; i < discardPile.size(); i++)
			{
				if (discardPile.get(i).isType("Duration"))
				{
					result.add(discardPile.get(i));
				}
			}
			return result;
		}
		else if (parameter.equals("Reaction"))
		{
			for (int i = 0; i < discardPile.size(); i++)
			{
				if (discardPile.get(i).isType("Reaction"))
				{
					result.add(discardPile.get(i));
				}
			}
			return result;
		}
		else if (parameter.equals("Curse"))
		{
			for (int i = 0; i < discardPile.size(); i++)
			{
				if (discardPile.get(i).isType("Curse"))
				{
					result.add(discardPile.get(i));
				}
			}
			return result;
		}
		else
		{
			for (int i = 0; i < discardPile.size(); i++)
			{
				if (discardPile.get(i).getCardName().equals(parameter))
				{
					result.add(discardPile.get(i));
				}
			}
			return result;
		}
	}
	
	public void startTurn()
	{
		TextLogger.setIndent(0);
		
		TextLogger.record("\n" + name + "'s Turn " + turns);
		isThisPlayersTurn = true;
		TextLogger.setIndent(1);
		
		if (play.size() != 0)
		{
			for (DomCard c : play)
			{
				c.playNextTurn();
			}
		}
		
		int extraCards = friendshipTokens / 8;
		
		if (extraCards > 0)
		{
			TextLogger.record(name + " has " + friendshipTokens + " friendship.");
			drawCards(extraCards);
		}
	}
	
	public void endTurn()
	{
		//discard any cards in hand or play
		discardHandCleanup();
		discardCardsInPlay();
		//draw new hand. Doing it one card at a time gets around text recording
		for (int i = 0; i < 5; i++)
		{
			drawCard();
		}
		//increaseTurnsOfAllInPlay(); maybe used for durations
		//resets player characteristics
		actions = 1;
		buys = 1;
		money = 0;
		copperValue = 1;
		turns++;
		currentPhase = "Action";
		
		if (gameInterface.getSupply().getSupplyOfCard(new ProvinceCard()) == 0)
		{
			TextLogger.record("The game has ended on Provinces!");
			gameInterface.concludeGame();
		}
		else if (gameInterface.getSupply().getNumberEmptyPiles() >= 3)
		{
			TextLogger.record("The game has ended on Piles!");
			gameInterface.concludeGame();
		}
		
		isThisPlayersTurn = false;
		hasBoughtCardThisTurn = false;
		
		gameInterface.startNextPlayerTurn();
	}
	
	/**
	 * Tells the player what supply is used.
	 * @param cardsUsed
	 */
	public void setSupply(Supply cardsUsed)
	{
		supply = cardsUsed;
	}
	
	private void discardCardsInPlay() 
	{
		ArrayList<DomCard> cardsToKeep = new ArrayList<DomCard>();
		
		for (int i = 0; i < play.size(); i++)
		{
			DomCard testedCard = play.get(i);
			if (!testedCard.isType("Duration") || (testedCard.durationEffectPlayed()))
			{
				testedCard.setPlayDurationEffect(false);
				discardPile.add(testedCard);
			}
			else if (testedCard.isType("Duration") && (!testedCard.durationEffectPlayed()))
			{
				testedCard.setPlayDurationEffect(true);
				cardsToKeep.add(testedCard);
			}
		}
		
		play.clear();
		play.addAll(cardsToKeep);
	}
	
	/**
	 * Draws n cards
	 * @param n Number of cards to draw
	 */
	public void drawCards(int n)
	{
		if (totalCardsInDrawAndDiscard() < n)
		{
			drawCards(totalCardsInDrawAndDiscard());
		}
		else
		{
			for (int i = 0; i < n; i++)
			{
				drawCard();
			}
			
			if (n == 1)
			{
				TextLogger.record(name + " draws 1 card.");
			}
			else
			{
				TextLogger.record(name + " draws " + n + " cards.");
			}
		}
	}
	
	/**
	 * Draws one card
	 */
	public void drawCard()
	{
		if (totalCardsInDrawAndDiscard() != 0)
		{
			if (deckEmpty())
			{
				reshuffle();
			}
			DomCard card = (DomCard) deck.get(0);
			moveCardFromZoneToZone(card, deck, hand);
		}
	}
	
	public DomCard getFirstRevealedCard()
	{
		if (revealedCards.size() == 0)
		{
			return null;
		}
		else
		{
			return revealedCards.get(0);
		}
	}
	
	public DomCard getLastRevealedCard()
	{
		if (revealedCards.size() == 0)
		{
			return null;
		}
		else
		{
			return revealedCards.get(revealedCards.size() - 1);
		}
	}
	
	/**
	 * Reveals n cards from the top of your draw pile.
	 * THIS METHOD SHOULD NOT BE CALLED: USE revealCards instead. 
	 * Revealed cards are set aside in another zone. You must call a return cards method to bring them back.
	 * @param n Number of cards to reveal.
	 */
	public void revealCardsFromTop(int n)
	{
		if (totalCardsInDrawAndDiscard() < n)
		{
			revealCardsFromTop(totalCardsInDrawAndDiscard());
		}
		else
		{
			for (int i = 0; i < n; i++)
			{
				revealCardFromTop();
			}
			
			if (n == 0)
			{
				TextLogger.record(name + " reveals nothing.");
			}
			else
			{
				String log = name + " reveals";
				for (int i = revealedCards.size() - 1; i >= revealedCards.size() - n; i--)
				{
					log += " " + revealedCards.get(i).getCardName();
				}
				log += ".";
				TextLogger.record(log);
			}
		}
	}
	
	/**
	 * Reveals 1 card from the top of draw pile
	 *
	 */
	public void revealCardFromTop()
	{
		if (totalCardsInDrawAndDiscard() == 0)
		{
			TextLogger.record("No cards to reveal.");
		}
		else
		{
			if (deckEmpty())
			{
				reshuffle();
			}
			DomCard card = deck.get(0);
			moveCardFromZoneToZone(card, deck, revealedCards);
		}
	}
	
	public void discardCardsFromTop(int n)
	{
		for (int i = 0; i < n; i++)
		{
			discardCardFromTop();
		}
	}
	
	public void discardCardFromTop()
	{
		if (totalCardsInDrawAndDiscard() == 0)
		{
			TextLogger.record("No cards to discard from deck.");
		}
		else
		{
			if (deckEmpty())
			{
				reshuffle();
			}
			deck.get(0).discard();
			moveCardFromZoneToZone(deck.get(0), deck, discardPile);
		}
	}
	
	/**
	 * Reveals n cards from the bottom of the draw pile
	 *@param n
	 */
	public void revealCardsFromBottom(int n)
	{
		for (int i = 0; i < n; i++)
		{
			revealCardFromBottom();
		}
	}
	
	/**
	 * Reveals 1 card from the bottom of the draw pile
	 */
	private void revealCardFromBottom() 
	{
		if (totalCardsInDrawAndDiscard() == 0)
		{
			TextLogger.record("No cards to reveal");
		}
		else
		{
			if (deckEmpty())
			{
				reshuffle();
			}
			DomCard card = deck.get(deck.size() - 1);
			moveCardFromZoneToZone(card, revealedCards, hand);
		}
	}
	
	/**
	 * Discards all revealed cards
	 */
	public void discardRevealedCards()
	{
		discardPile.addAll(revealedCards);
		String record = getName() + " discards";
		for (int i = 0; i < revealedCards.size(); i++)
		{
			revealedCards.get(i).discard();
			record += revealedCards.get(i).getCardName();
		}
		revealedCards.clear();
		TextLogger.record(record);
	}
	
	/**
	 * Returns the revealed cards to the top of the deck, in the order they were originally in
	 */
	public void returnRevealedCards()
	{
		DomCard card;
		for (int i = revealedCards.size() - 1; i >= 0; i--)
		{
			card = revealedCards.get(i);
			deck.add(0, card);
		}
		revealedCards.clear();
	}
	
	/**
	 * Discards hand
	 */
	public void discardHandCleanup()
	{
		discardPile.addAll(hand);
		hand.clear();
	}
	
	/**
	 * Discards the cards with the given names. 
	 * This method assumes that the hand contains the cards with the given names
	 * @param namesOfCards The names of cards to discard
	 */
	public void discardCardsWithNames(ArrayList<String> namesOfCards)
	{
		boolean discarded;
		for (int i = 0; i < namesOfCards.size(); i++)
		{
			discarded = false;
			for (int j = 0; j < hand.size(); j++)
			{
				if (!discarded)
				{
					if (hand.get(j).getCardName().equals(namesOfCards.get(i)))
					{
						hand.get(j).discard();
						moveCardFromZoneToZone(hand.get(j), hand, discardPile);
						discarded = true;
					}
				}
			}
		}
	}
	
	/**
	 * Finds the index of the requested card in the player's hand
	 * @param card The card to find
	 * @return The index of the card. Returns -1 if the card cannot be found
	 */
	public int inHand(DomCard card)
	{
		return inZone(card, hand);
	}
	
	/**
	 * Gains card from supply to the discard pile
	 * @param card The card to be gained
	 */
	public void gainCard(DomCard card)
	{
		gainCardToZone(card, discardPile);
	}
	
	/**
	 * Gains card to the specified zone
	 * @param card The card to be gained
	 * @param zone The zone the card will go to
	 */
	public void gainCardToZone(DomCard card, ArrayList<DomCard> zone)
	{
		if (supply.lookUpSupply(card) > 0)
		{
			//Need to gain a copy of the card, so we clone it
			DomCard cardCopy = card.clone();
			cardCopy.setOwner(this);
			zone.add(cardCopy);
			supply.doGainEvents(card);
		}
	}
	
	/**
	 * Shuffles draw and discard together
	 * Does so for any given draw and discard, not just an empty draw pile.
	 */
	public void reshuffle() 
	{
		deck.addAll(discardPile);
		discardPile.clear();
		int rand;
		DomCard tempCard;
		
		for (int i = deck.size() - 1; i > 0; i--)
		{
		  rand = (int) ((i+1) * Math.random());
		  tempCard = (DomCard) deck.get(i);
		  deck.set(i, deck.get(rand));
		  deck.set(rand, tempCard);
		}
		/*
		for (int i = 0; i < deck.size(); i++)
		{
			rand1 = (int) (getCardsInDeck() * Math.random());
			rand2 = (int) (getCardsInDeck() * Math.random());
			tempCard = (DomCard) deck.get(rand1);
			deck.set(rand1, deck.get(rand2));
			deck.set(rand2, tempCard);
		}
		*/
		TextLogger.record("(" + name + " reshuffles)");
	}
	
	//Methods that count the number of cards
	public boolean deckEmpty()
	{
		return (getCardsInDeck() == 0);
	}
	
	//Below useful for situations like a 4-card deck and Philosopher's Stone
	public int totalCardsInDrawAndDiscard()
	{
		return (getCardsInDeck()+ getCardsInDiscard());
	}
	
	//Methods that return what a player has
	public ArrayList<DomCard> getDeck()
	{
		return deck;
	}
	
	public ArrayList<DomCard> getHand()
	{
		return hand;
	}
	
	public ArrayList<DomCard> getPlay()
	{
		return play;
	}
	
	public ArrayList<DomCard> getDiscard()
	{
		return discardPile;
	}
	
	public ArrayList<DomCard> getSetAside()
	{
		return setAside;
	}
	
	public ArrayList<DomCard> getRevealed()
	{
		return revealedCards;
	}
	
	//Methods that returns the number of cards in the given zone
	public int getCardsInDeck()
	{
		return deck.size();
	}
	
	public int getCardsInHand()
	{
		return hand.size();
	}
	
	public int getCardsInPlay()
	{
		return play.size();
	}
	
	public int getCardsInDiscard()
	{
		return discardPile.size();
	}
	
	public int getCardsInSetAside()
	{
		return setAside.size();
	}
	
	public int getCardsInRevealed()
	{
		return revealedCards.size();
	}
	
	public int getVPtokens()
	{
		return victoryPointTokens;
	}
	
	public int getActions()
	{
		return actions;
	}
	
	public int getBuys()
	{
		return buys;
	}
	
	public int getMoney()
	{
		return money;
	}
	
	public int getFriendship()
	{
		return friendshipTokens;
	}
	
	public int getCopperValue()
	{
		return copperValue;
	}
	
	/**
	 * Returns the first index of a card in a zone. -1 if card is not in that zone
	 * @param card The card in question
	 * @param zone The zone
	 * @return The index of the card, or -1
	 */
	public int inZone(DomCard card, ArrayList<DomCard> zone)
	{
		for (int i = 0; i < zone.size(); i++)
		{
			if (card.equals(zone.get(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Removes a card from the first zone, and then adds it to the end of the 2nd zone.
	 * It moves the first instance of that card.
	 * If the card doesn't exist in zone 1, nothing happens
	 * @param card The card to move
	 * @param zone1 The name of the first zone
	 * @param zone2 The name of the second zone
	 */
	public void moveCardFromZoneToZone(DomCard card, ArrayList<DomCard> zone1, ArrayList<DomCard> zone2)
	{
		boolean moved = false;
		int i = 0;
		while(!moved && i < zone1.size())
		{
			if (zone1.get(i).equals(card))
			{
				zone2.add(zone1.remove(i));
				moved = true;
			}
			i++;
		}
	}
	
	public String getPhase()
	{
		return currentPhase;
	}
	
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets the phase to the given string
	 * @param phase The phase the player is in
	 */
	public void setPhase(String phase)
	{
		currentPhase = phase;
	}
	
	public String toString()
	{
		return "Player";
	}
	
	/**
	 * Shows which cards are buyable this turn
	 * @param supply The supply of cards
	 */
	public void showBuyable()
	{
		for (int i = 0; i < supply.getCards().size(); i++)
		{
			supply.getCards().get(i).calcCanBuy();
		}
	}
	
	public void discardCards(int numberToDiscard)
	{
		gameInterface.repaint();
		
		if (hand.size() <= numberToDiscard)
		{
			lastChoices.clear();
			
			for (int i = 0; i < hand.size(); i++)
			{
				lastChoices.add(hand.get(i).getCardName());
			}
			applyOptions(DISCARD_PURPOSE, lastChoices);
		}
		else
		{
			OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), new OptionSelector(this, numberToDiscard, DISCARD_PURPOSE, hand));
			d.pack();
			d.setLocation(DIALOG_X_START, DIALOG_Y_START);
			d.setVisible(true);
		}
		
		TextLogger.record(name + " discards " 
				+ lastChoices.size() + " cards.");
	}
	
	public void setAllowedToAct(boolean allowed)
	{
		allowedToPlayOrBuy = allowed;
	}
	
	public boolean isAllowedToAct()
	{
		return allowedToPlayOrBuy;
	}
	
	public void applyOptions(String endPurpose,
			ArrayList<String> selectedOptions) 
	{
		lastChoices = selectedOptions;
		
		if (endPurpose.startsWith(CHOICE_PURPOSE))
		{
			//do nothing
		}
		else if (endPurpose.startsWith(DISCARD_PURPOSE))
		{
			discardCardsWithNames(selectedOptions);
		}
		else if (endPurpose.startsWith(TRASH_PURPOSE))
		{
			trashCardsWithNames(selectedOptions);
		}
		
		gameInterface.getOptionSelector().setVisible(false);
		gameInterface.repaint();
	}
	
	private void trashCardsWithNames(ArrayList<String> selectedOptions) 
	{
		
		boolean discarded;
		for (int i = 0; i < selectedOptions.size(); i++)
		{
			discarded = false;
			for (int j = 0; j < hand.size(); j++)
			{
				if (!discarded)
				{
					if (hand.get(j).getCardName().equals(selectedOptions.get(i)))
					{
						moveCardFromZoneToZone(hand.get(j), hand, trash);
						discarded = true;
					}
				}
			}
		}	
	}
	
	public void ask(int numberToChoose, String ... choices)
	{
		gameInterface.repaint();
		
		OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), new OptionSelector(this, numberToChoose, CHOICE_PURPOSE, choices));
		d.pack(); 
		d.setLocation(DIALOG_X_START, DIALOG_Y_START);
		d.setVisible(true);
		
		d.dispose();
	}
	
	public void askOpponents(int numberToChoose, String ... choices)
	{
		for (int i = 0; i < allPlayers.length; i++)
		{
			gameInterface.setDisplayedPlayer(i);
			gameInterface.repaint();
			
			if (allPlayers[i] != this)
			{
				allPlayers[i].ask(numberToChoose, choices);
			}
		}
		
		gameInterface.setDisplayedPlayer(gameInterface.getCurrentPlayer());
	}
	
	public void trashExactlyNCards(int n) 
	{
		gameInterface.repaint();
		
		if (hand.size() <= n)
		{
			lastChoices.clear();
			
			for (int i = 0; i < hand.size(); i++)
			{
				lastChoices.add(hand.get(i).getCardName());
			}
			
			applyOptions("Trash Exactly", lastChoices);
		}
		else
		{
			OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), new OptionSelector(this, n, "Trash Exactly", hand));
			d.pack(); 
			d.setLocation(DIALOG_X_START, DIALOG_Y_START);
			d.setVisible(true);
			
			d.dispose();
		}
		
		String log = name + " trashes";
		
		for (int i = 0; i < lastChoices.size(); i++)
		{
			log += " " + lastChoices.get(i);
		}
		
		TextLogger.record(log);
	}
	
	public void trashExactlyNCards(int n, ArrayList<DomCard> zone) 
	{
		gameInterface.repaint();
		
		if (hand.size() <= n)
		{
			lastChoices.clear();
			
			for (int i = 0; i < hand.size(); i++)
			{
				lastChoices.add(hand.get(i).getCardName());
			}
			
			applyOptions("Trash Exactly", lastChoices);
		}
		else
		{
			OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), new OptionSelector(this, n, "Trash Exactly", hand));
			d.pack(); 
			d.setLocation(DIALOG_X_START, DIALOG_Y_START);
			d.setVisible(true);
			
			d.dispose();
		}
		
		String log = name + " trashes";
		
		for (int i = 0; i < lastChoices.size(); i++)
		{
			log += " " + lastChoices.get(i);
		}
		
		TextLogger.record(log);
	}
	
	public void trashUpToNCards(int n)
	{
		gameInterface.repaint();
		
		OptionSelector s = new OptionSelector(this, n, "Trash Up To", hand);
		s.setWhetherWantExactlyGoal(false);
		
		OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), s);
		d.pack();
		d.setLocation(DIALOG_X_START, DIALOG_Y_START);
		d.setVisible(true);
		
		d.dispose();
		
		String log = name + " trashes";
		
		if (lastChoices.size() == 0)
		{
			log += " nothing";
		}
		
		for (int i = 0; i < lastChoices.size(); i++)
		{
			log += " " + lastChoices.get(i);
		}
		
		log += ".";
		TextLogger.record(log);
	}
	
	public PlaytesterDisplay getInterface()
	{
		return gameInterface;
	}
	
	public ArrayList<String> getChoices()
	{
		return lastChoices;
	}
	
	/**
	 * Reveals cards from the player's hand
	 * @param n The number of cards to reveal
	 */
	public void revealCardsFromHand(int n) 
	{
		if (getCardsInHand() <= n)
		{
			lastChoices.clear();
			
			for (int i = 0; i < getCardsInHand(); i++)
			{
				lastChoices.add(hand.get(i).getCardName());
			}
		}
		else
		{
			gameInterface.repaint();
			
			OptionSelector s = new OptionSelector(this, n, CHOICE_PURPOSE, hand);
			OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), s);
			d.pack();
			d.setLocation(DIALOG_X_START, DIALOG_Y_START);
			d.setVisible(true);
			
			d.dispose();
		}
		
		String log = name + " reveals";
		for (int i = 0; i < lastChoices.size(); i++)
		{
			log += " " + lastChoices.get(i);
		}
		log += ".";
		TextLogger.record(log);
	}
	
	public void ask(int numberToChoose, ArrayList<DomCard> allCardsOwnedByPlayer) 
	{
		gameInterface.repaint();
		
		if (allCardsOwnedByPlayer.size() <= numberToChoose)
		{
			for (int i = 0; i < allCardsOwnedByPlayer.size(); i++)
			{
				lastChoices.clear();
				lastChoices.add(allCardsOwnedByPlayer.get(i).getCardName());
			}
			
			applyOptions(CHOICE_PURPOSE, lastChoices);
		}
		else
		{
			OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), new OptionSelector(this, numberToChoose, CHOICE_PURPOSE, allCardsOwnedByPlayer));
			d.pack(); 
			d.setLocation(DIALOG_X_START, DIALOG_Y_START);
			d.setVisible(true);
			
			d.dispose();
		}
	}
	
	public void choosePlayer()
	{
		gameInterface.repaint();
		
		String[] playerNames = new String[allPlayers.length];
		
		for (int i = 0; i < allPlayers.length; i++)
		{
			playerNames[i] = allPlayers[i].getName();
			
		}
		OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), new OptionSelector(this, 1, CHOICE_PURPOSE, playerNames));
		d.pack(); 
		d.setLocation(DIALOG_X_START, DIALOG_Y_START);
		d.setVisible(true);
		
		d.dispose();
		TextLogger.record("...choosing " + lastChoices.get(0));
	}
	
	public void nameCard() 
	{
		gameInterface.repaint();
		
		OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), new OptionSelector(this, 1, CHOICE_PURPOSE, gameInterface.getUsedCards()));
		d.pack(); 
		d.setLocation(DIALOG_X_START, DIALOG_Y_START);
		d.setVisible(true);
		
		d.dispose();
	}
	
	public void opponentsMayTrashCards(int n)
	{
		for (int i = 0; i < allPlayers.length; i++)
		{
			if (!(allPlayers[i] == this))
			{
				allPlayers[i].trashUpToNCards(1);
			}
		}
	}
	
	public void letOpponentsRevealReactions()
	{
		for (int i = 0; i < allPlayers.length; i++)
		{
			if (!(allPlayers[i] == this))
			{
				allPlayers[i].revealReactions();
			}
		}
	}
	
	public boolean revealedCard(DomCard card)
	{
		gameInterface.repaint();
		
		if (searchZone(hand, card.getCardName()).size() != 0)
		{
			OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), new OptionSelector(this, 1, CHOICE_PURPOSE, "Reveal " + card.getCardName(), "Don't Reveal"));
			d.pack(); 
			d.setLocation(DIALOG_X_START, DIALOG_Y_START);
			d.setVisible(true);
			
			d.dispose();
			
			if ((!lastChoices.get(0).equals("Don't Reveal")))
			{
				TextLogger.record("..." + name + "reveals a " + card.getCardName());
				return true;
			}
			else return false;
		}
		else return false;
		
	}
	
	public void revealReactions()
	{
		gameInterface.repaint();
		ArrayList<DomCard> reactions = searchZone(hand, "Reaction");
		
		if (reactions.size() != 0)
		{
			//We need an option to not reveal the reaction, so we cannot use the direct method for creating the selector
			
			String[] reactionNames = new String[reactions.size() + 1];
			for (int i = 0; i < reactions.size(); i++)
			{
				reactionNames[i] = reactions.get(i).getCardName();
			}
			reactionNames[reactions.size()] = "none";
			
			OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), new OptionSelector(this, 1, CHOICE_PURPOSE, reactionNames));
			d.pack(); 
			d.setLocation(DIALOG_X_START, DIALOG_Y_START);
			d.setVisible(true);
			
			d.dispose();
			
			String name = lastChoices.get(0);
			
			//keep revealing reactions until the player decides not to
			if (!name.equals("none"))
			{
				DomCard reaction = gameInterface.lookUpCard(name);
				playReactionEffectFromHand(reaction);
				revealReactions();
			}
		}
	}
	
	private void playReactionEffectFromHand(DomCard reaction) 
	{
		int i = 0;
		boolean done = false;
		
		while ((i < hand.size()) && !done)
		{
			if (hand.get(i).equals(reaction))
			{
				hand.get(i).playReactionEffect();
				done = true;
			}
			i++;
		}
	}
	
	public void revealHand()
	{
		String log = "...reveals";
		for (int i = 0; i < hand.size(); i++)
		{
			log += " " + hand.get(i).getCardName();
		}
		TextLogger.record(log);
	}
	
	public void setAffectedByAttacks(boolean b) 
	{
		affectedByAttacks = b;
	}
	
	public boolean isAffectedByAttacks()
	{
		return affectedByAttacks;
	}
	
	public void opponentsAffectedByAttacksGainCard(DomCard card)
	{
		ArrayList<Player> opp = getOpponentsAffectedByAttacks();
		
		for (int i = 0; i < opp.size(); i ++)
		{
			opp.get(i).gainCard(card);
			TextLogger.record(opp.get(i).getName() + " gains a " + card.getCardName() + ".");
		}
	}
	
	public void resetAffectedByAttacks() 
	{
		for (int i = 0; i < allPlayers.length; i++)
		{
			allPlayers[i].setAffectedByAttacks(true);
		}
	}
	
	/**
	 * Returns the opponents which revealed the card
	 * @param card The card to reveal
	 * @return A list of players who revealed that card
	 */
	public ArrayList<Player> letOpponentsRevealCard(DomCard card)
	{
		ArrayList<Player> opp = new ArrayList<Player>();
		
		for (int i = 0; i < allPlayers.length; i++)
		{
			if (!(allPlayers[i] == this) &&
					(allPlayers[i].revealedCard(card)))
			{
				opp.add(allPlayers[i]);
			}
		}
		
		return opp;
	}
	
	public int getTotalVP()
	{
		ArrayList<DomCard> allVictoryCards = new ArrayList<DomCard>();
		allVictoryCards.addAll(searchZone(hand, "Victory"));
		allVictoryCards.addAll(searchZone(discardPile, "Victory"));
		allVictoryCards.addAll(searchZone(deck, "Victory"));
		allVictoryCards.addAll(searchZone(play, "Victory"));
		allVictoryCards.addAll(searchZone(setAside, "Victory"));
		allVictoryCards.addAll(searchZone(revealedCards, "Victory"));
		
		ArrayList<DomCard> allCurseCards = new ArrayList<DomCard>();
		allCurseCards.addAll(searchZone(hand, "Curse"));
		allCurseCards.addAll(searchZone(discardPile, "Curse"));
		allCurseCards.addAll(searchZone(deck, "Curse"));
		allCurseCards.addAll(searchZone(play, "Curse"));
		allCurseCards.addAll(searchZone(setAside, "Curse"));
		allCurseCards.addAll(searchZone(revealedCards, "Curse"));
		
		int total = 0;
		
		for (int i = 0; i < allVictoryCards.size(); i++)
		{
			allVictoryCards.get(i).calculateVP();
			total += allVictoryCards.get(i).getVP();
		}
		
		for (int i = 0; i < allCurseCards.size(); i++)
		{
			allCurseCards.get(i).calculateVP();
			total += allCurseCards.get(i).getVP();
		}
		
		totalVP = total;
		return total;
	}
	
	public void discardAnyNumberOfCards()
	{
		gameInterface.repaint();
		OptionSelector s = new OptionSelector(this, hand.size(), "Discard any number", hand);
		s.setWhetherWantExactlyGoal(false);
		
		OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(),s);
		d.pack();
		d.setLocation(DIALOG_X_START,DIALOG_Y_START);
		d.setVisible(true);
		
		d.dispose();
		
		TextLogger.record(name + " discards " + lastChoices.size() + " cards.");
	}
	
	public void nameCardThisCostOrLower(int n)
	{
		ArrayList<DomCard> allCardsUsed = gameInterface.getUsedCards();
		ArrayList<DomCard> validCards = new ArrayList<DomCard>();
		DomCard card;
		
		for (int i = 0; i < allCardsUsed.size(); i++)
		{
			card = allCardsUsed.get(i);
			if (card.getCost() <= n)
			{
				validCards.add(card);
			}
		}
		
		gameInterface.repaint();
		
		OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), new OptionSelector(this, 1, CHOICE_PURPOSE, validCards));
		d.pack(); 
		d.setLocation(DIALOG_X_START, DIALOG_Y_START);
		d.setVisible(true);
		
		d.dispose();
	}
	
	/**
	 * Accepts basic types. Start with a - to do all but
	 * @param n The cost
	 * @param type The type
	 */
	public void nameCardThisTypeAndCostOrLower(int n, String type)
	{
		ArrayList<DomCard> allCardsUsed = gameInterface.getUsedCards();
		ArrayList<DomCard> validCards = new ArrayList<DomCard>();
		DomCard card;
		boolean allButThisType = type.startsWith("-");
		String goalType = type;
		if (allButThisType)
		{
			goalType = goalType.substring(1);
		}
		
		boolean isGoalType;
		
		for (int i = 0; i < allCardsUsed.size(); i++)
		{
			card = allCardsUsed.get(i);
			isGoalType = card.isType(goalType);
			
			if (card.getCost() <= n)
			{
				if (isGoalType == !allButThisType)
				{
					validCards.add(card);
				}
			}
		}
		
		gameInterface.repaint();
		
		OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), new OptionSelector(this, 1, CHOICE_PURPOSE, validCards));
		d.pack(); 
		d.setLocation(DIALOG_X_START, DIALOG_Y_START);
		d.setVisible(true);
		
		d.dispose();
	}
	
	public int nameNumber()
	{
		NumberDialog d = new NumberDialog(getInterface().findParentFrame(), this);
		d.pack();
		d.setLocation(DIALOG_X_START, DIALOG_Y_START);
		d.setVisible(true);
		
		d.dispose();
		
		return Integer.parseInt(lastChoices.get(0));
		/*
		 String s = (String) JOptionPane.showInputDialog(
		 gameInterface.findParentFrame(),
		 "Choose a number from 0 to 99. No decimals, no spaces.",
		 "Choose a Number",
		 JOptionPane.PLAIN_MESSAGE,
		 null,
		 null,
		 "");
		 
		 int length = s.length();
		 
		 if (length > 2)
		 {
		 JOptionPane.showMessageDialog(gameInterface.findParentFrame(),
		 "Your input was too long.",
		 "Error",
		 JOptionPane.ERROR_MESSAGE);
		 return nameNumber();
		 }
		 else
		 {
		 for (int i = 0; i < length; i++)
		 {
		 if ((s.charAt(i) > '9') ||
		 (s.charAt(i) < '0'))
		 {
		 JOptionPane.showMessageDialog(gameInterface.findParentFrame(),
		 "Your input contains a non-digit.",
		 "Error",
		 JOptionPane.ERROR_MESSAGE);
		 return nameNumber();
		 }
		 }
		 return Integer.parseInt(s);
		 }
		 */
	}
	
	/**
	 * Returns all cards owned by this player. Does not actually move the cards.
	 * @return
	 */
	public ArrayList<DomCard> getAllCardsOwned()
	{
		ArrayList<DomCard> allCardsOwnedByPlayer = new ArrayList<DomCard>();
		allCardsOwnedByPlayer.addAll(hand);
		allCardsOwnedByPlayer.addAll(deck);
		allCardsOwnedByPlayer.addAll(discardPile);
		allCardsOwnedByPlayer.addAll(play);
		allCardsOwnedByPlayer.addAll(revealedCards);
		allCardsOwnedByPlayer.addAll(setAside);
		
		return allCardsOwnedByPlayer;
	}
	
	public void trashHand()
	{
		trash.addAll(hand);
		hand.clear();
	}
	
	public void trashDeck()
	{
		trash.addAll(deck);
		deck.clear();
	}
	
	public void trashDiscard()
	{
		trash.addAll(discardPile);
		discardPile.clear();
	}
	
	public void trashPlay()
	{
		trash.addAll(play);
		play.clear();
	}
	
	public void trashRevealed()
	{
		trash.addAll(revealedCards);
		revealedCards.clear();
	}
	
	public void trashSetAside()
	{
		trash.addAll(setAside);
		setAside.clear();
	}
	
	public void trashAllCardsOwned()
	{
		trashHand();
		trashDeck();
		trashDiscard();
		trashPlay();
		trashRevealed();
		trashSetAside();
	}
	
	/**
	 * Accepts basic types.
	 * 
	 * @param type The type of card or name of card
	 */
	public void revealCardsUntilThisTypeRevealed(String type)
	{
		boolean satisfied = false;
		DomCard card;
		
		while ((totalCardsInDrawAndDiscard() >= 0) &&
				!satisfied)
		{
			revealCardsFromTop(1);
			card = getLastRevealedCard();
			
			if (card.isType(type))
			{
				satisfied = true;
			}
		}
	}
	
	public void returnLastRevealedCard()
	{
		if (revealedCards.size() > 0)
		{
			deck.add(0, revealedCards.remove(revealedCards.size() - 1));
		}
	}
	
	public void revealCardOfThisType(String type)
	{
		ArrayList<DomCard> cards = new ArrayList<DomCard>();
		DomCard card;
		
		for (int i = 0; i < getHand().size(); i++)
		{
			card = getHand().get(i);
			
			if (card.isType(type))
			{
				cards.add(card);
			}
		}
		
		if (cards.size() > 0)
		{
			gameInterface.repaint();
			
			OptionSelector s = new OptionSelector(this, 1, CHOICE_PURPOSE, cards);
			OptionSelectorDialog d = new OptionSelectorDialog(gameInterface.findParentFrame(), s);
			d.pack();
			d.setLocation(DIALOG_X_START, DIALOG_Y_START);
			d.setVisible(true);
			
			d.dispose();
			
			TextLogger.record(name + " reveals " + lastChoices.get(0));
		}
	}
	
	public Supply getSupply()
	{
		return supply;
	}
	
	public void chooseCard(ArrayList<DomCard> cards)
	{
		ask(1,cards);
	}
}
