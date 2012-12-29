package cardData;
import framework.DomCard;

public class SilverCard extends DomCard {
	
	public SilverCard()
	{
		super(3,0,0,"Silver","Treasure");
	}
	
	public void play()
	{
		getOwner().addMoney(2);
	}

	public SilverCard clone()
	{
		return new SilverCard();
	}
}
