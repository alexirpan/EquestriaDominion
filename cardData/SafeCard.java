package cardData;
import framework.DomCard;
import framework.TextLogger;

public class SafeCard extends DomCard {
	
	public SafeCard()
	{
		super(5,0,0,"Safe","Action", "Duration");
	}
	
	@Override
	public void play()
	{
		getOwner().drawCards(1);
		getOwner().addActions(1);
		getOwner().addMoney(1);
		TextLogger.record(getOwner().getName() + " gets +$1.");
	}
	
	public void playNextTurn()
	{
		TextLogger.record("Safe duration effect");
		getOwner().addMoney(1);
		TextLogger.record(getOwner().getName() + " gets +$1.");
	}

	public SafeCard clone()
	{
		return new SafeCard();
	}
}
