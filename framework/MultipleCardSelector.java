package framework;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class uses multiple card selectors to let players input which cards he/she wants to select for some purpose
 * 
 * 
 * @author alexI
 *
 */
public class MultipleCardSelector extends JPanel {
	
	private CardSelector[] cardCheckBoxes;
	private int numberSelected;
	private int goalNumber;
	private ArrayList<DomCard> selectedCards = new ArrayList<DomCard>();
	private boolean rightAmountSelected;
	private DoneButton done = new DoneButton();
	
	
	public MultipleCardSelector()
	{
		this(null, 0);
	}
	
	/**
	 * Creates a CardSelector for each card in the list, as well as a button that appears when the given number of items are selected
	 * @param cardList
	 * @param howManyToSelect
	 */
	public MultipleCardSelector(ArrayList<DomCard> cardList, int howManyToSelect)
	{
		super(new GridLayout(1,0));
		cardCheckBoxes = new CardSelector[cardList.size()];
		for (int i = 0; i < cardList.size(); i++)
		{
			cardCheckBoxes[i] = new CardSelector(cardList.get(i));
			this.add(cardCheckBoxes[i]);
		}
		this.add(done);
		numberSelected = 0;
		goalNumber = howManyToSelect;
		rightAmountSelected = false;
	}

	public void calcAppropriateNumberSelected()
	{
		numberSelected = 0;
		for (int i = 0; i < cardCheckBoxes.length; i++)
		{
			if (cardCheckBoxes[i].isSelected())
			{
				numberSelected++;
			}
		}
		if (numberSelected == goalNumber)
		{
			rightAmountSelected = true;
			selectedCards.clear();
			for (int i = 0; i < cardCheckBoxes.length; i++)
			{
				if (cardCheckBoxes[i].isSelected())
				{
					selectedCards.add(cardCheckBoxes[i].getCard());
				}
			}
		}
		else
		{
			rightAmountSelected = false;
		}
	}
	
	/**
	 * Should only be called if the right number of boxes are selected
	 * 
	 * This can be verified by isRightAmountSelected()
	 * @return
	 */
	public ArrayList<DomCard> getSelectedCards()
	{
		return selectedCards;
	}
	
	public boolean isRightAmountSelected()
	{
		return rightAmountSelected;
	}
	
	class DoneButton extends JButton implements ActionListener
	{
		public DoneButton()
		{
			super("done");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) 
		{
			calcAppropriateNumberSelected();
		}
	}
}
