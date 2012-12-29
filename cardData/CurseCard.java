package cardData;
import framework.DomCard;

public class CurseCard extends DomCard {
	
	public CurseCard()
	{
		super(0,0,-1,"Curse","Curse");
	}
	
	public void play()
	{
		
	}
	
	public CurseCard clone()
	{
		return new CurseCard();
	}
}
