package framework;

import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class InfoDialog extends JDialog{
	
	private JOptionPane optionPane;
	
	/** Creates the reusable dialog. */
	public InfoDialog(Frame aFrame, ArrayList<String> info) {
		super(aFrame, true);
		setTitle("Info");
		
//		Create the JOptionPane.
		optionPane = new JOptionPane();
		optionPane.removeAll();
		
		optionPane.setLayout(new GridLayout(0,1));
		for (String s : info)
		{
			optionPane.add(new JLabel(s));
		}
		
//		Make this dialog display it.
		setContentPane(optionPane);
		
//		Handle window closing correctly.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
