package framework;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Creates a component that lets the player select a card
 * @author alexI
 *
 */
public class CardSelector extends JCheckBox implements ItemListener {
	
	private DomCard card;
	private JCheckBox button;
	
	
	public CardSelector()
	{
		this(null);
	}
	
	public CardSelector(DomCard cardToSelect)
	{
		super(cardToSelect.getCardName());
		card = cardToSelect;

		addItemListener(this);
	}

	public void itemStateChanged(ItemEvent arg0) 
	{
		
	}
	
	public DomCard getCard()
	{
		return card;
	}
	
}
