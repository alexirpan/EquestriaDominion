package cardData;
import java.util.ArrayList;

import framework.DomCard; 
import framework.SupplyCardComponent;
import framework.TextLogger;

public class GrandGallopingGalaCard extends DomCard {
	
	public GrandGallopingGalaCard()
	{
		super(5,0,0,"Grand Galloping Gala","Action");
	}
	
	@Override
	public void play()
	{
		//add "Can only buy if gold in play" and flavor
		getOwner().drawCards(1);
		getOwner().addActions(1);
		getOwner().addBuys(1);
		getOwner().addMoney(2);
		TextLogger.record(getOwner().getName() + " gets +$2.");
		getOwner().addFriendship(1);
		
		getOwner().ask(1, "Everybody draws", "Nobody draws");
		
		if (getOwner().getChoices().get(0).equals("Everybody draws"))
		{
			TextLogger.record(getOwner().getName()+ " lets everyone come to the Gala.");
			getOwner().drawCards(1);
			getOwner().opponentsDrawCards(1);
		}
	}

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
						ArrayList<DomCard> treasures = scc.getCurrentPlayer().searchZone(
								scc.getCurrentPlayer().getPlay(), "Treasure");
						
						for (int i = 0; i < treasures.size(); i++)
						{
							if (!treasures.get(i).equals(new GoldCard()))
							{
								return false;
							}
						}
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
	
	public GrandGallopingGalaCard clone()
	{
		return new GrandGallopingGalaCard();
	}
}
