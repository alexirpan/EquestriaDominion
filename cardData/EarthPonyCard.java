package cardData;
import java.util.Collections;

import framework.DomCard;
import framework.TextLogger;

public class EarthPonyCard extends DomCard {
	
	public EarthPonyCard()
	{
		super(4,0,0,"Earth Pony","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().revealHand();
		boolean no5 = true;
		
		for (DomCard d : getOwner().getHand())
		{
			if (d.getCost() >= 5)
			{
				no5 = false;
			}
		}
		
		if (no5)
		{
			getOwner().drawCards(3);
		}
		else
		{
			getOwner().drawCards(2);
			getOwner().addMoney(1);
			TextLogger.record(getOwner().getName() + " gets +$1.");
		}
	}

	public EarthPonyCard clone()
	{
		return new EarthPonyCard();
	}
}
