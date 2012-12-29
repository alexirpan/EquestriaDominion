package cardData;
import framework.DomCard;
import framework.TextLogger;

public class PegasusCard extends DomCard {
	
	public PegasusCard()
	{
		super(4,0,0,"Pegasus","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().addMoney(2);
		TextLogger.record(getOwner().getName() + " gets +$2.");
		getOwner().addFriendship(2);
	}

	public PegasusCard clone()
	{
		return new PegasusCard();
	}
}
