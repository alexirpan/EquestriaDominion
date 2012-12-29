package cardData;
import java.util.ArrayList; 

import framework.DomCard;
import framework.PlaytesterDisplay;
import framework.TextLogger;

public class RebirthCard extends DomCard {
	
	public RebirthCard()
	{
		super(7,0,0,"Rebirth","Action");
	}
	
	@Override
	public void play()
	{
		getOwner().ask(10, getOwner().getAllCardsOwned());
		
		ArrayList<DomCard> chosenCards = new ArrayList<DomCard>();
		for (int i = 0; i < getOwner().getChoices().size(); i++)
		{
			chosenCards.add(PlaytesterDisplay.lookUpCard(getOwner().getChoices().get(i)));
			//the cards looked up from the interface have no owner, so we have to set the owner as well
			chosenCards.get(i).setOwner(getOwner());
		}
		//first trash all cards, then delete copies of the chosen cards from the trash for the sake of keeping the trash consistent
		getOwner().trashAllCardsOwned();
		
		
		getOwner().getDiscard().addAll(chosenCards);
		
		String log = getOwner().getName() + " chooses";
		
		for (int i = 0; i < getOwner().getChoices().size(); i++)
		{
			log += " " + getOwner().getChoices().get(i);
		}
		log += ".";
		TextLogger.record(log);
	}

	public RebirthCard clone()
	{
		return new RebirthCard();
	}
}
