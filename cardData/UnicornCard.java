package cardData;
import framework.DomCard;
import framework.TextLogger;

public class UnicornCard extends DomCard {
	
	public UnicornCard()
	{
		super(4,0,0,"Unicorn","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().addBuys(1);
		getOwner().addMoney(3);
		TextLogger.record(getOwner().getName() + " gets +$3.");
		getOwner().opponentsDrawCards(1);
	}

	public UnicornCard clone()
	{
		return new UnicornCard();
	}
}
