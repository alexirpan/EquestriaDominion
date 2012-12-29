package cardData;
import java.util.ArrayList;

import framework.DomCard;
import framework.TextLogger;

public class WorkhorseCard extends DomCard {
	
	public WorkhorseCard()
	{
		super(2,0,0,"Workhorse","Action");
	}
	
	@Override
	public void play()
	{
		//Seems too good. 
		//DEFINITELY TOO GOOD. HOOOOOO, WHY SO OP
		//Changed +2 buys to +1 buy. 
		//If only one player goes for Workhorse, seems too strong. 
		//And it also provides the +buy you need to get lots of Workhorses
		getOwner().addFriendship(1);
		
		getOwner().ask(2, "+1 Card", "+2 Actions", "+$2", "+1 Buy");
		
		ArrayList<String> choices = getOwner().getChoices();
		for (int i = 0; i < choices.size(); i++)
		{
			if (choices.get(i).equals("+1 Card"))
			{
				getOwner().drawCards(1);
			}
			else if (choices.get(i).equals("+2 Actions"))
			{
				getOwner().addActions(2);
			}
			else if (choices.get(i).equals("+$2"))
			{
				getOwner().addMoney(2);
				TextLogger.record(getOwner().getName() + " gets +$2.");
			}
			else if (choices.get(i).equals("+1 Buy"))
			{
				getOwner().addBuys(1);
			}
			else
			{
				System.out.println("something went wrong");
			}
		}
			 
		getOwner().opponentsDrawCards(1);
	}

	public WorkhorseCard clone()
	{
		return new WorkhorseCard();
	}
}
