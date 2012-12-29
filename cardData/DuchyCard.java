package cardData;
import framework.DomCard;

public class DuchyCard extends DomCard {
	
	public DuchyCard()
	{
		super(5,0,3,"Duchy","Victory");
	}
	
	public void play()
	{
		
	}

	public DuchyCard clone()
	{
		return new DuchyCard();
	}
}
