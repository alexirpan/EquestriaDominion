package cardData;
import java.util.ArrayList;

import framework.DomCard;
import framework.Player;
import framework.TextLogger;

public class CutieMarkCard extends DomCard {
	
	public CutieMarkCard()
	{
		super(4,0,0,"Cutie Mark","Action","Attack");
	}
	
	@Override
	public void play()
	{
		getOwner().drawCards(2);
		
		ArrayList<Player> notBlankFlanks = getOwner().letOpponentsRevealCard(this);
		
		//You automatically reveal the Cutie Mark you play
		int cursesToGive = notBlankFlanks.size() + 1;
		
		for (int i = 0; i < getOwner().getOpponentsAffectedByAttacks().size(); i++)
		{
			Player p = getOwner().getOpponentsAffectedByAttacks().get(i);
			
			if (!(notBlankFlanks.contains(p)))
			{
				//then you are a blank flank and you will suffer
				for (int j = 0; j < cursesToGive; j++)
				{
					p.drawCards(1);
					p.gainCard(new CurseCard());
				}
				
				if (cursesToGive == 1)
					TextLogger.record(p.getName() + " gains a Curse.");
				else
					TextLogger.record(p.getName() + " gains " + cursesToGive + " Curses.");
			}
		}
		
		getOwner().resetAffectedByAttacks();
	}

	public CutieMarkCard clone()
	{
		return new CutieMarkCard();
	}
}
