package cardData;
import framework.DomCard;
import framework.TextLogger;

public class StewCard extends DomCard {
	
	public StewCard()
	{
		super(3,0,0,"Stew","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().addMoney(1);
		TextLogger.record(getOwner().getName() + " gets +$1.");
		
		getOwner().trashUpToNCards(2);
		getOwner().opponentsMayTrashCards(1);
	}

	public StewCard clone()
	{
		return new StewCard();
	}
}
