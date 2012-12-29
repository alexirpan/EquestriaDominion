package framework;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This code is very much based off the custom dialog used in the Oracle tutorials
 * So credit goes to them for all the nice documentation and such.
 * Including the documentation that doesn't make sense because of the stuff I changed
 */
public class NumberDialog extends JDialog implements PropertyChangeListener {
	
	private JOptionPane optionPane;
	private JTextField textField;
	private JLabel info;
	private Player askedPlayer;
	
	/** Creates a dialog that names a number from 0 to 99*/
	public NumberDialog(Frame aFrame, Player p) {
		super(aFrame, true);
		setTitle(p.getName() + " Name a Number");
		askedPlayer = p;
		
		textField = new JTextField(10);
		info = new JLabel("Choose a number from 0 to 99. "
				+ "No decimals or spaces.");
		
		JPanel panel = new JPanel();
		panel.add(info);
		panel.add(textField);
		
//		Create the JOptionPane.
		optionPane = new JOptionPane(panel,
				JOptionPane.PLAIN_MESSAGE);
		
//		Make this dialog display it.
		setContentPane(optionPane);
		
//		Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
//		Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				textField.requestFocusInWindow();
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
			else
			{
				String s = textField.getText();
				int length = s.length();

				if (length > 2)
				{
					JOptionPane.showMessageDialog(askedPlayer.getInterface().findParentFrame(),
							"Your input was too long.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
				else if (length == 0)
				{
					JOptionPane.showMessageDialog(askedPlayer.getInterface().findParentFrame(),
							"Please enter something.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					boolean valid = true;
					for (int i = 0; i < length; i++)
					{
						if ((s.charAt(i) > '9') ||
								(s.charAt(i) < '0'))
						{
							JOptionPane.showMessageDialog(askedPlayer.getInterface().findParentFrame(),
									"Your input contains a non-digit.",
									"Error",
									JOptionPane.ERROR_MESSAGE);
							valid = false;
						}
					}
					
					if (valid)
					{
						optionPane.setValue(textField.getText());
						askedPlayer.lastChoices.clear();
						askedPlayer.lastChoices.add(textField.getText());
						clearAndHide();
					}
				}
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
