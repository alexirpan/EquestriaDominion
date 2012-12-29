package cardData;
import framework.DomCard;

public class CopperCard extends DomCard {
	
	public CopperCard()
	{
		super(0,0,0,"Copper","Treasure");
	}
	
	@Override
	public void play()
	{
		getOwner().addMoney(getOwner().getCopperValue());
	}

	public CopperCard clone()
	{
		return new CopperCard();
	}
}
