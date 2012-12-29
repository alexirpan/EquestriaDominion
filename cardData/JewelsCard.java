package cardData;
import framework.DomCard;
import framework.PlaytesterDisplay;
import framework.TextLogger;

public class JewelsCard extends DomCard {

	public JewelsCard()
	{
		super(3,0,0,"Jewels","Treasure");
	}

	@Override
	public void play()
	{
		getOwner().trashExactlyNCards(1);

		if (getOwner().getChoices().size() == 1)
		{
			DomCard card = PlaytesterDisplay.lookUpCard(getOwner().getChoices().get(0));
			int value = card.getCost() / 2;
			getOwner().addMoney(value);
			TextLogger.record(getOwner().getName() + " gets $" + value);
		}
	}

	public JewelsCard clone()
	{
		return new JewelsCard();
	}
}
