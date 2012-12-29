package cardData;
import framework.DomCard;
import framework.PlaytesterDisplay;
import framework.TextLogger;

public class WeatherFactoryCard extends DomCard {
	
	public WeatherFactoryCard()
	{
		super(4,0,0,"Weather Factory","Action");
	}
	
	public void play()
	{
		int n = getOwner().nameNumber();
		
		getOwner().opponentsDrawCards(n);
		getOwner().nameCardThisTypeAndCostOrLower(4 + n,"-Victory");
		
		DomCard card = PlaytesterDisplay.lookUpCard(getOwner().getChoices().get(0));
		getOwner().gainCard(card);
		TextLogger.record(getOwner().getName() + " gains a " + card.getCardName());
	}
	
	public void calculateVP()
	{
		setVP(getOwner().getFriendship() / 4);
	}

	public WeatherFactoryCard clone()
	{
		return new WeatherFactoryCard();
	}
}
