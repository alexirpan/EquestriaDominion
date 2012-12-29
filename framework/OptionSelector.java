package framework;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class allows the user to choose some number of choices from a list of options
 * Works on straight text or on lists of cards
 * Takes a string as a purpose, then returns that string when the correct number of options are filled.
 * 
 * @author alexI
 *
 */
public class OptionSelector extends JPanel {
	
	private JCheckBox[] optionCheckBoxes;
	private int numberSelected;
	private int goalNumber;
	private ArrayList<String> selectedOptions = new ArrayList<String>();
	private boolean rightAmountSelected;
	private boolean completedGoal = false;
	private String endPurpose = "";
	private Player affectedPlayer;
	private boolean exactlyGoal = true;
	
	/**
	 * This method is used only for the purposes of initialization
	 *
	 */
	public OptionSelector()
	{
		this(null, 1, "test", "test");
	}
	
	public OptionSelector(Player person, int howManyToSelect, String purpose, String ... options)
	{
		super(new FlowLayout());
		
		numberSelected = 0;
		goalNumber = howManyToSelect;
		endPurpose = purpose;
		affectedPlayer = person;
		rightAmountSelected = false;
		JCheckBox option;
		
		addLabel();
		optionCheckBoxes = new JCheckBox[options.length];
		for (int i = 0; i < options.length; i++)
		{
			option = new JCheckBox(options[i]);
			optionCheckBoxes[i] = option;
			add(option);
		}
	}
	
	/**
	 * Creates a selector for each card in the list, as well as a button that appears when the given number of items are selected
	 * @param cardList
	 * @param howManyToSelect
	 */
	public OptionSelector(Player person, int howManyToSelect, String purpose, ArrayList<DomCard> cardList)
	{
		super(new FlowLayout());
		
		numberSelected = 0;
		goalNumber = howManyToSelect;
		endPurpose = purpose;
		affectedPlayer = person;
		rightAmountSelected = false;
		
		addLabel();
		optionCheckBoxes = new JCheckBox[cardList.size()];
		JCheckBox option;
		for (int i = 0; i < cardList.size(); i++)
		{
			option = new JCheckBox(cardList.get(i).getCardName());
			optionCheckBoxes[i] = option;
			add(option);
		}
	}
	
	public void setGoalAmount(int goalAmount)
	{
		goalNumber = goalAmount;
	}
	
	public void setWhetherWantExactlyGoal(boolean goal)
	{
		exactlyGoal = goal;
	}
	
	public void setOptions(String ... options)
	{
		this.removeAll();
		JCheckBox option;
		addLabel();
		optionCheckBoxes = new JCheckBox[options.length];
		for (int i = 0; i < options.length; i++)
		{
			option = new JCheckBox(options[i]);
			optionCheckBoxes[i] = option;
			add(option);
		}
	}
	
	public void setPurpose(String purpose)
	{
		endPurpose = purpose;
	}
	
	public String getPurpose()
	{
		return endPurpose;
	}
	
	public void setOptions(ArrayList<DomCard> cardList)
	{
		this.removeAll();
		addLabel();
		optionCheckBoxes = new JCheckBox[cardList.size()];
		JCheckBox option;
		for (int i = 0; i < cardList.size(); i++)
		{
			option = new JCheckBox(cardList.get(i).getCardName());
			optionCheckBoxes[i] = option;
			add(option);
		}
	}
	
	public void addLabel()
	{
		//The specific cases here are the only cases that have been needed so far
		if (endPurpose.equals("Discard"))
		{
			add(new JLabel("Discard " + goalNumber + " cards"));
		}
		else if (endPurpose.equals("Choice"))
		{
			add(new JLabel("Choose " + goalNumber));
		}
		else if (endPurpose.equals("Trash Exactly"))
		{
			add(new JLabel("Trash " + goalNumber + " cards"));
		}
		else if (endPurpose.equals("Trash Up To"))
		{
			add(new JLabel("Trash up to " + goalNumber + " cards"));
		}
		else
		{
			add(new JLabel(endPurpose));
		}
	}
	
	public void setPlayer(Player person)
	{
		affectedPlayer = person;
	}
	
	public void paintComponent(Graphics g)
	{
		g.clearRect(0,0,this.getWidth(), this.getHeight());
		calcAppropriateNumberSelected();
	}
	
	public void calcAppropriateNumberSelected()
	{
		numberSelected = 0;
		for (int i = 0; i < optionCheckBoxes.length; i++)
		{
			if (optionCheckBoxes[i].isSelected())
			{
				numberSelected++;
			}
		}
		
		if (exactlyGoal)
		{
			if (numberSelected == goalNumber)
			{
				rightAmountSelected = true;
			}
			else
			{
				rightAmountSelected = false;
			}
		}
		else
		{
			if (numberSelected <= goalNumber)
			{
				rightAmountSelected = true;
			}
			else
			{
				rightAmountSelected = false;
			}
		}
		repaint();
	}
	
	/**
	 * Should only be called if the right number of boxes are selected
	 * 
	 * This can be verified by isRightAmountSelected()
	 * @return The options selected
	 */
	public ArrayList<String> getSelectedOptions()
	{
		return selectedOptions;
	}
	
	public boolean isRightAmountSelected()
	{
		return rightAmountSelected;
	}
	
	public boolean isComplete()
	{
		return completedGoal;
	}
	
	public void calculateSelectedChoices()
	{
		selectedOptions.clear();
		for (int i = 0; i < optionCheckBoxes.length; i++)
		{
			if (optionCheckBoxes[i].isSelected())
			{
				selectedOptions.add(optionCheckBoxes[i].getText());
			}
		}
		
		/**
		 * Prevents accidental calls after work is done
		 */
		if (!completedGoal)
		{
			completedGoal = true;
			affectedPlayer.applyOptions(endPurpose, selectedOptions);
		}
	}
	
	public Player getPlayer()
	{
		return affectedPlayer;
	}
	
	class DoneButton extends JButton implements ActionListener
	{
		public DoneButton()
		{
			super("Not done");
			addActionListener(this);
		}
		
		public void updateText()
		{
			if (rightAmountSelected)
			{
				setText("Done");
			}
			else
			{
				setText("Not done");
			}
		}
		
		
		public void actionPerformed(ActionEvent arg0) 
		{
			if (getText().equals("Done"))
			{
				calculateSelectedChoices();
			}
		}
	}
}
