package cardData;
import framework.DomCard;

public class GoldCard extends DomCard {
	
	public GoldCard()
	{
		super(6,0,0,"Gold","Treasure");
	}
	
	public void play()
	{
		getOwner().addMoney(3);
	}

	public GoldCard clone()
	{
		return new GoldCard();
	}
}
