package cardData;
import java.util.ArrayList;

import framework.DomCard;
import framework.PlaytesterDisplay;
import framework.SupplyCardComponent;
import framework.TextLogger;

public class DiggerCard extends DomCard {
	
	public DiggerCard()
	{
		super(4,0,0,"Digger","Treasure");
	}
	
	@Override
	public void play()
	{
		getOwner().addMoney(1);
		TextLogger.increaseIndent(1);
		
		boolean satisfied = false;
		DomCard card;
		getOwner().nameCardThisTypeAndCostOrLower(100,"Treasure");
		DomCard namedCard = PlaytesterDisplay.lookUpCard(getOwner().getChoices().get(0));
		
		while ((getOwner().totalCardsInDrawAndDiscard() >= 0) &&
				!satisfied)
		{
			getOwner().revealCardsFromTop(1);
			card = getOwner().getLastRevealedCard();
			//System.out.println(card);
			
			if (card.equals(namedCard))
			{
				getOwner().returnLastRevealedCard();
				getOwner().discardRevealedCards();
				satisfied = true;
			}
			
			//System.out.println(getOwner().totalCardsInDrawAndDiscard());
			//System.out.println(satisfied);
		}
		
		TextLogger.increaseIndent(-1);
	}

	public DiggerCard clone()
	{
		return new DiggerCard();
	}
}
