package cardData;
import framework.DomCard;
import framework.PlaytesterDisplay;
import framework.TextLogger;

public class PonyvilleCard extends DomCard {
	
	public PonyvilleCard()
	{
		super(4,0,0,"Ponyville","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().drawCards(1);
		getOwner().addActions(2);
		
		getOwner().nameCard();
		TextLogger.record(getOwner().getName() + " names " + getOwner().getChoices().get(0));
		
		getOwner().revealCardsFromTop(1);
		
		DomCard namedCard = PlaytesterDisplay.lookUpCard(getOwner().getChoices().get(0));
		DomCard revealedCard = getOwner().getLastRevealedCard();
		
		if (revealedCard != null)
		{
			if (namedCard.equals(revealedCard))
			{
				getOwner().returnRevealedCards();
				getOwner().drawCards(1);
			}
			else
			{
				getOwner().returnRevealedCards();
			}
		}
	}
	
	public PonyvilleCard clone()
	{
		return new PonyvilleCard();
	}
}
