package cardData;
import framework.DomCard;
import framework.TextLogger;

public class SatchelCard extends DomCard {
	
	public SatchelCard()
	{
		super(4,0,0,"Satchel","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().revealCardsFromTop(1);
		DomCard card = getOwner().getFirstRevealedCard();
		getOwner().returnRevealedCards();
		
		if (card.isType("Victory"))
		{
			getOwner().drawCards(3);
		}
		
		if (card.isType("Treasure"))
		{
			getOwner().addActions(1);
			getOwner().addMoney(1);
			TextLogger.record(getOwner().getName() + " gets +$1.");
			//The below is playing a card in the non-abstracted way. This circumvents checking for the buy phase
			getOwner().moveCardFromZoneToZone(card, getOwner().getDeck(), getOwner().getPlay());
			card.play();
			TextLogger.record(getOwner().getName() + " plays a " + card.getCardName());
		}
		
		if (card.isType("Action"))
		{
			getOwner().drawCards(1);
			getOwner().addActions(2);
		}
	}

	public SatchelCard clone()
	{
		return new SatchelCard();
	}
}
