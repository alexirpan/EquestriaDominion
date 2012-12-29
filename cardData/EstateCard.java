package cardData;
import framework.DomCard;

public class EstateCard extends DomCard {
	
	public EstateCard()
	{
		super(2,0,1,"Estate","Victory");
	}
	
	public void play()
	{
		
	}
	
	public EstateCard clone()
	{
		return new EstateCard();
	}
}
