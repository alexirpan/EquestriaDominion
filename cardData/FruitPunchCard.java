package cardData;
import framework.DomCard;
import framework.TextLogger;

public class FruitPunchCard extends DomCard {
	
	public FruitPunchCard()
	{
		super(5,0,0,"Fruit Punch","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().addActions(1);
		
		getOwner().ask(1, "+$2", "Discard and Draw");
		
		if (getOwner().getChoices().get(0).equals("+$2"))
		{
			getOwner().addMoney(2);
			TextLogger.record(getOwner().getName() + " gets +$2.");
		}
		else
		{
			getOwner().discardHandCleanup();
			TextLogger.record(getOwner().getName() + " discards hand.");
			
			getOwner().opponentsDrawCard();
			getOwner().choosePlayer();
			
			int draw = 0;
			
			for (int i = 0; i < getOwner().getPlayers().length; i++)
			{
				if (getOwner().getPlayers()[i].getName().equals(getOwner().getChoices().get(0)))
				{
					draw = getOwner().getPlayers()[i].getCardsInHand() - getOwner().getCardsInHand();
				}
			}
			
			getOwner().drawCards(draw);
		}
	}

	public FruitPunchCard clone()
	{
		return new FruitPunchCard();
	}
}
