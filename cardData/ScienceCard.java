package cardData;
import java.util.ArrayList;

import framework.DomCard;
import framework.PlaytesterDisplay;
import framework.TextLogger;

public class ScienceCard extends DomCard {
	
	public ScienceCard()
	{
		super(5,0,0,"Science","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().discardAnyNumberOfCards();
		
		int cardsDiscarded = getOwner().getChoices().size();
		
		if (cardsDiscarded >= 8)
		{
			getOwner().gainCard(new ProvinceCard());
			TextLogger.record(getOwner().getName() + " gains a Province.");
		}
		else if (cardsDiscarded >= 4)
		{
			getOwner().gainCard(new GoldCard());
			TextLogger.record(getOwner().getName() + " gains a Gold.");
		}
		else if (cardsDiscarded >= 2)
		{
			getOwner().gainCard(new SilverCard());
			TextLogger.record(getOwner().getName() + " gains a Silver.");
		}
	}

	public ScienceCard clone()
	{
		return new ScienceCard();
	}
}
