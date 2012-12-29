package framework;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * This code is very much based off the custom dialog used in the Oracle tutorials
 * So credit goes to them for all the nice documentation and such.
 * Including the documentation that doesn't make sense because of the stuff I changed
 */
public class OptionSelectorDialog extends JDialog implements PropertyChangeListener {
	
	private OptionSelector select;

	private JOptionPane optionPane;
	
	
	/** Creates the reusable dialog. */
	public OptionSelectorDialog(Frame aFrame, OptionSelector s) {
		super(aFrame, true);
		setTitle(s.getPlayer().getName() + " " + s.getPurpose());
		
		select = s;
		
//		Create an array specifying the number of dialog buttons
//		and their text.
		
//		Create the JOptionPane.
		optionPane = new JOptionPane(select,
				JOptionPane.PLAIN_MESSAGE);
		
//		Make this dialog display it.
		setContentPane(optionPane);
		
//		Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
//		Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				select.requestFocusInWindow();
			}
		});
		
//		Register an event handler that reacts to option pane state changes.
		optionPane.addPropertyChangeListener(this);
	}
	
	/** This method reacts to state changes in the option pane. */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		if (isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) ||
						JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
			
			if (optionPane.getValue() == JOptionPane.UNINITIALIZED_VALUE)
			{
				//do nothing
				return;
			}
			else if (select.isRightAmountSelected())
			{
				select.calculateSelectedChoices();
				optionPane.setValue(select.getSelectedOptions());
				clearAndHide();
			}
			else
			{
				JOptionPane.showMessageDialog(
                        OptionSelectorDialog.this,
                        "Please select the right number of options.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
			}
			
//			Reset the JOptionPane's value.
//			If you don't do this, then if the user
//			presses the same button next time, no
//			property change event will be fired.
			optionPane.setValue(
					JOptionPane.UNINITIALIZED_VALUE);
			
			
		}
	}
	
	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		getContentPane().removeAll();
		setVisible(false);
	}

}
