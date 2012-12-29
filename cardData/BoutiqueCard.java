package cardData;
import framework.DomCard; 

public class BoutiqueCard extends DomCard {
	
	public BoutiqueCard()
	{
		super(4,0,0,"Boutique","Action","Duration");
	}
	
	//The timing issues of how long to maintain an improved copper is not implemented exactly as it should be
	//As of now, this approximation works well enough
	
	public void play()
	{
		//increase copper value for all players
		for (int i = 0; i < getOwner().getPlayers().length; i++)
		{
			getOwner().getPlayers()[i].increaseCopperValue(1);
		}
	}
	
	public void playNextTurn()
	{
		//increase copper value only for the owner of the card
		for (int i = 0; i < getOwner().getPlayers().length; i++)
		{
			if (getOwner().getPlayers()[i] == getOwner())
			{
			getOwner().getPlayers()[i].increaseCopperValue(1);
			}
		}
	}

	public BoutiqueCard clone()
	{
		return new BoutiqueCard();
	}
}
