package cardData;

import framework.DomCard;
import framework.TextLogger;

public class ParaspriteCard extends DomCard {
	
	public ParaspriteCard()
	{
		super(3,0,0,"Parasprite","Action", "Attack");
	}
	
	@Override
	public void play()
	{
		getOwner().addMoney(2);
		TextLogger.record(getOwner().getName() + " gets +$2");
		getOwner().trashExactlyNCards(1);
		getOwner().opponentsAffectedByAttacksGainCard(this);
		getOwner().resetAffectedByAttacks();
	}

	public ParaspriteCard clone()
	{
		return new ParaspriteCard();
	}
}
