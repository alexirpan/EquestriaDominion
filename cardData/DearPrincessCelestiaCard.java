package cardData;
import framework.DomCard;
import framework.Player;
import framework.TextLogger;

public class DearPrincessCelestiaCard extends DomCard {
	
	public DearPrincessCelestiaCard()
	{
		super(2,0,0,"Dear Princess Celestia","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().addActions(1);
		
		int cardsDiscarded;
		
		getOwner().discardAnyNumberOfCards();
		cardsDiscarded = getOwner().getChoices().size();

		getOwner().drawCards(cardsDiscarded);
		
		getOwner().discardAnyNumberOfCards();
		cardsDiscarded = getOwner().getChoices().size();
		
		getOwner().drawCards(getOwner().getChoices().size());
		
		getOwner().askOpponents(1, "Discard 2 cards", "Don't discard 2 cards");
		
		Player[] opp = getOwner().getPlayers();
		
		for (int i = 0; i < opp.length; i++)
		{			 
			if (opp[i] != getOwner())
			{
				if (opp[i].getChoices().get(0).startsWith("Discard"))
				{
					opp[i].discardCards(2);
					opp[i].drawCards(1);
					//TextLogger.record(opp[i].getName() + " discards 2 cards and draws a card");
				}
			}
		}
	}

	public DearPrincessCelestiaCard clone()
	{
		return new DearPrincessCelestiaCard();
	}
}
