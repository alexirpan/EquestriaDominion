package cardData;
import framework.DomCard;
import framework.TextLogger;

public class CloudsdaleCard extends DomCard {
	
	public CloudsdaleCard()
	{
		super(5,0,0,"Cloudsdale","Action","Duration");
	}
	
	@Override
	public void play()
	{
		getOwner().drawCards(3);
		getOwner().addBuys(1);
		getOwner().opponentsDrawCards(1);
		getOwner().opponentsGetFriendship(1);
	}
	
	public void playNextTurn()
	{
		TextLogger.record("Cloudsdale was played last turn.");
		getOwner().drawCards(2);
		getOwner().addBuys(1);
	}

	public CloudsdaleCard clone()
	{
		return new CloudsdaleCard();
	}
}
