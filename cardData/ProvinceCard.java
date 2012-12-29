package cardData;
import framework.DomCard;

public class ProvinceCard extends DomCard {
	
	public ProvinceCard()
	{
		super(8,0,6,"Province","Victory");
	}
	
	public void play()
	{
		
	}

	public ProvinceCard clone()
	{
		return new ProvinceCard();
	}
}
