package cardData;
import java.util.ArrayList;

import framework.DomCard;
import framework.TextLogger;

public class ElementsOfHarmonyCard extends DomCard {
	
	public ElementsOfHarmonyCard()
	{
		super(2,0,0,"Elements Of Harmony","Action", "Reaction");
	}
	
	@Override
	public void play()
	{
		getOwner().addMoney(1);
		TextLogger.record(getOwner().getName() + " gets +$1.");
		getOwner().addFriendship(2);
	}
	
	public void playReactionEffect()
	{
		getOwner().ask(1, "Discard Elements", "Reveal Elements");
		if (getOwner().getChoices().get(0).equals("Discard Elements"))
		{
			ArrayList<String> alist = new ArrayList<String>();
			alist.add("Elements Of Harmony");
			getOwner().discardCardsWithNames(alist);
		}
		else if (getOwner().getFriendship() >= 8)
		{
			getOwner().setAffectedByAttacks(false);
		}
	}
	
	public void discard()
	{
		getOwner().addFriendship(2);
	}

	public ElementsOfHarmonyCard clone()
	{
		return new ElementsOfHarmonyCard();
	}
}
