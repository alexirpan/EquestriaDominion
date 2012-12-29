package cardData;
import java.util.ArrayList;

import framework.DomCard;
import framework.Player;
import framework.PlaytesterDisplay;
import framework.TextLogger;

public class ShowboaterCard extends DomCard {
	
	public ShowboaterCard()
	{
		super(4,0,0,"Showboater","Action","Attack");
	}
	
	@Override
	public void play()
	{
		ArrayList<Player> opp = getOwner().getOpponentsAffectedByAttacks();
		ArrayList<DomCard> nonVictoryCards = new ArrayList<DomCard>();
		for (int i = 0; i < opp.size(); i++)
		{
			opp.get(i).revealCardsFromTop(2);
			
			DomCard card1 = opp.get(i).getFirstRevealedCard();
			DomCard card2 = opp.get(i).getLastRevealedCard();
			//TextLogger.record(opp.get(i).getName() + " discards " + card2.getCardName() + " and " + card1.getCardName());
						
			for (int j = 0; j < opp.get(i).getRevealed().size(); j++)
			{
				if (!(opp.get(i).getRevealed().get(j).isType("Victory")))
				{
					nonVictoryCards.add(opp.get(i).getRevealed().get(j));
				}
			}
			/*
			for (int j = 0; j < nonVictoryCards.size(); j++)
			{
				getOwner().gainCard(nonVictoryCards.get(j));
				TextLogger.record(getOwner().getName() + " gains a " + nonVictoryCards.get(j).getCardName());
			}
			*/
			/*
			if (nonVictoryCards.size() <= 1)
			{
				for (int j = 0; j < nonVictoryCards.size(); j++)
				{
					getOwner().gainCard(nonVictoryCards.get(j));
					TextLogger.record(getOwner().getName() + " gains a " + nonVictoryCards.get(j).getCardName());
				}
			}
			else
			{
				getOwner().ask(1, "Gain 1st card.", "Gain 2nd card.");
				
				if (getOwner().getChoices().get(0).equals("Gain 1st card."))
				{
					getOwner().gainCard(card1);
					TextLogger.record(getOwner().getName() + " gains a " + card1.getCardName());
					
					opp.get(i).gainCard(card2);
					TextLogger.record(opp.get(i).getName() + " gains a " + card2.getCardName());
				}
				else
				{
					getOwner().gainCard(card2);
					TextLogger.record(getOwner().getName() + " gains a " + card2.getCardName());
					
					opp.get(i).gainCard(card1);
					TextLogger.record(opp.get(i).getName() + " gains a " + card1.getCardName());
				}
			}
			*/
			opp.get(i).discardRevealedCards();
		}
		if (nonVictoryCards.size() > 0)
		{
			getOwner().chooseCard(nonVictoryCards);
			DomCard namedCard = PlaytesterDisplay.lookUpCard(getOwner().getChoices().get(0));
			getOwner().gainCard(namedCard);
			TextLogger.record(getOwner().getName() + " gains a " + namedCard.getCardName());
		}
		getOwner().resetAffectedByAttacks();
	}
	
	public ShowboaterCard clone()
	{
		return new ShowboaterCard();
	}
}
