package cardData;
import framework.DomCard; 

public class CanterlotCard extends DomCard {
	
	public CanterlotCard()
	{
		super(6,0,0,"Canterlot","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().drawCards(8);
		getOwner().opponentsDrawCards(4);
	}

	public CanterlotCard clone()
	{
		return new CanterlotCard();
	}
}
