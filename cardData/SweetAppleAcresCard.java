package cardData;
import framework.DomCard; 

public class SweetAppleAcresCard extends DomCard {
	
	public SweetAppleAcresCard()
	{
		super(5,0,0,"Sweet Apple Acres","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().drawCards(3);
		getOwner().addActions(1);
		getOwner().addFriendship(1);
				
		getOwner().opponentsDrawCards(1);
	}

	public SweetAppleAcresCard clone()
	{
		return new SweetAppleAcresCard();
	}
}
