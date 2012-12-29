package framework;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Creates a component that lets the player select from a list of cards
 * @author alexI
 *
 */
public class SelectorComponent extends JComponent implements ItemListener {
	
	private ArrayList<DomCard> cardList;
	private JCheckBox[] buttons;
	
	
	public SelectorComponent()
	{
		this(null);
	}
	
	public SelectorComponent(ArrayList<DomCard> cards)
	{
		cardList = cards;
		buttons = new JCheckBox[cardList.size()];
		initializeButtons();
	}
	
	public void initializeButtons()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			buttons[i] = new JCheckBox(cardList.get(i).getCardName());
			buttons[i].addItemListener(this);
			add(buttons[i]);
		}
	}

	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	


}
