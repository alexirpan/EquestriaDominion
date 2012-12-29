package cardData;
import framework.DomCard;
import framework.Player;
import framework.TextLogger;

public class IronPonyCompetitionCard extends DomCard {

	public IronPonyCompetitionCard()
	{
		super(3,0,0,"Iron Pony Competition","Action");
	}

	@Override
	public void play()
	{
		getOwner().addActions(2);
		getOwner().addMoney(1);
		TextLogger.record(getOwner().getName() + " gets +$1.");

		int bestCost = -1;
		boolean ownerHasBest = false;
		DomCard revealedCard;
		
		for (Player p : getOwner().getPlayers())
		{
			p.revealCardsFromTop(1);
			
			//returns null if no cards revealed.
			revealedCard = p.getLastRevealedCard();
			
			if (revealedCard != null)
			{
				/*
				System.out.println(revealedCard);
				System.out.println(bestCost);
				*/
				
				if (revealedCard.getCost() >= bestCost)
				{
					if (p == getOwner())
					{
						ownerHasBest = true;
					}
					else if (revealedCard.getCost() > bestCost) //ties must favor owner
					{
						ownerHasBest = false;
					}
					
					bestCost = revealedCard.getCost();
				}
			}
		}

		for (Player p : getOwner().getPlayers())
		{
			p.returnRevealedCards();
		}

		if (ownerHasBest)
		{
			TextLogger.record(getOwner().getName() + " is the iron pony!");
			getOwner().drawCards(1);
		}
		else
		{
			TextLogger.record(getOwner().getName() + " is not the iron pony.");
		}
	}

	public IronPonyCompetitionCard clone()
	{
		return new IronPonyCompetitionCard();
	}
}
