package framework;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

public class PlayerViewer extends JComponent implements ActionListener
{
	ButtonGroup playerView = new ButtonGroup();
	PlaytesterDisplay gameInterface;
	
	public PlayerViewer(int numPlayers)
	{
		JRadioButton playerButton;
		
		for (int i = 0; i < numPlayers; i++)
		{
			playerButton = new JRadioButton("Player " + Integer.toString(i+1));
			playerButton.setActionCommand(Integer.toString(i));

			playerView.add(playerButton);
			playerButton.addActionListener(this);
			playerButton.setBounds(0, 0 + 32*i, 100, 32);

			if (i == 0)
			{
				playerButton.setSelected(true);
			}
			add(playerButton);
		}
	}
	
	public PlayerViewer(Player[] players)
	{
		JRadioButton playerButton;
		
		for (int i = 0; i < players.length; i++)
		{
			playerButton = new JRadioButton(players[i].getName());
			playerButton.setActionCommand(Integer.toString(i));

			playerView.add(playerButton);
			playerButton.addActionListener(this);
			playerButton.setBounds(0, 0 + 32*i, 100, 32);

			if (i == 0)
			{
				playerButton.setSelected(true);
			}
			add(playerButton);
		}
	}
	
	public void paintComponent(Graphics g)
	{
		for (int i = 0; i < getComponentCount(); i++)
		{
			getComponents()[i].repaint();
		}
	}

	public void actionPerformed(ActionEvent arg0) 
	{
		gameInterface.setDisplayedPlayer(Integer.decode(arg0.getActionCommand()));
		gameInterface.repaint();
	}
	
	public void setInterface(PlaytesterDisplay p)
	{
		gameInterface = p;
	}
}
