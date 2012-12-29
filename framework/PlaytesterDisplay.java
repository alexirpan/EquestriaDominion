package framework;
import java.awt.Color;  
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import cardData.*;

/**
 * Where the user can interact with the cards
 * @author alexI
 *
 */
public class PlaytesterDisplay extends JApplet implements MouseListener
{
	private Supply supply;
	private final int NUM_PLAYERS = 2;
	private final int APPLET_WIDTH = 825;
	private final int APPLET_HEIGHT = 640;
	private final int HAND_X_START = 300;
	private final int HAND_Y_START = 430;
	private final int HAND_X_SIZE = 350;
	private final int HAND_Y_SIZE = 200;
	private final int PLAYER_INFO_X_START = HAND_X_START;
	private final int PLAYER_INFO_Y_START = 285;
	private final int LOG_X_START = 300;
	private final int LOG_Y_START = 10;
	private final int PLAYER_VIEW_X = HAND_X_START + HAND_X_SIZE + 30;
	private final int PLAYER_VIEW_Y = 430;
	private final int OPTION_X_START = 200;
	private final int OPTION_Y_START = 0;
	private final int OPTION_WIDTH = 500;
	private final int OPTION_HEIGHT = PLAYER_INFO_Y_START - OPTION_Y_START - 10;
	private final int RESTART_X_START = PLAYER_VIEW_X;
	private final int RESTART_Y_START = PLAYER_VIEW_Y + 32*NUM_PLAYERS + 20;
	private final UsefulComparator SORT_FOR_USEFULNESS = new UsefulComparator();
	private static ArrayList<DomCard> listOfAllCards = new ArrayList<DomCard>();
	private static ArrayList<DomCard> listOfUsedCards = new ArrayList<DomCard>();
	private static Player[] players;
	private ArrayList<DomCardComponent> cardsInHand = new ArrayList<DomCardComponent>();
	private ArrayList<JLabel> textLog = new ArrayList<JLabel>();
	private static int currentPlayerIndex = 0; //index of array
	private static int displayedPlayerIndex = 0;
	DrawButton draw = new DrawButton();
	DeckButton viewDeck = new DeckButton();
	MoneyButton playTreasure = new MoneyButton();
	RestartButton restart = new RestartButton();
	InfoButton info = new InfoButton();
	PlayerViewer playerPanel;
	OptionSelector select = new OptionSelector();
	JTextArea text = new JTextArea();
	JScrollPane textDisplay = new JScrollPane(text);
	JPanel cardHolder = new JPanel();
	JScrollPane cardScroll = new JScrollPane(cardHolder);
	//private boolean allowedToTakeAction = true;
	
	public void init()
	{
		setSize(APPLET_WIDTH,APPLET_HEIGHT);
		initialSetUp();
		this.getContentPane().setLayout(null);
		playTreasure.setVisible(true);
		draw.setVisible(true);
		restart.setVisible(true);
		info.setVisible(true);
		playTreasure.setBounds(HAND_X_START, HAND_Y_START - 40, 64 ,32);
		draw.setBounds(HAND_X_START + 70, HAND_Y_START - 40, 96 ,32);
		restart.setBounds(RESTART_X_START, RESTART_Y_START, 96, 32);
		info.setBounds(HAND_X_START + 172, HAND_Y_START - 40, 64, 32);
		viewDeck.setBounds(HAND_X_START + 242, HAND_Y_START - 40, 128, 32);
		viewDeck.setVisible(false);
		select.setBounds(OPTION_X_START,OPTION_Y_START,OPTION_WIDTH,OPTION_HEIGHT);
		select.setVisible(false);
		add(draw);
		add(playTreasure);
		add(select);
		add(restart);
		add(info);
		add(viewDeck);
		text.setEditable(false);
		textDisplay.setBounds(LOG_X_START, LOG_Y_START, 500, 200);
		//textDisplay.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textDisplay.setPreferredSize(new Dimension(500, 200));
		add(textDisplay);
		cardHolder.setLayout(null);
		cardScroll.setBounds(HAND_X_START, HAND_Y_START,HAND_X_SIZE,HAND_Y_SIZE);
		cardScroll.setPreferredSize(new Dimension(HAND_X_SIZE, HAND_Y_SIZE));
		TextLogger.emptyLog();
		TextLogger.setIndent(0);
		TextLogger.record(players[0].getName() + "'s turn 1");
		TextLogger.increaseIndent(1);
	}
	
	private void initialSetUp() 
	{
		initializeCardList();
		initializeSupply();
		initializePlayers();
	}
	
	private void initializeSupply() 
	{
		listOfUsedCards.clear();
		//select the 10 cards used in this game
		int rand;
		for (int i = 0; i < 10; i++)
		{
			rand = (int) (listOfAllCards.size() * Math.random());
			listOfUsedCards.add(listOfAllCards.get(rand));
			listOfAllCards.remove(rand);
		}
		Collections.sort(listOfUsedCards);
		
		//add basic cards
		listOfUsedCards.add(new ProvinceCard());
		listOfUsedCards.add(new DuchyCard());
		//listOfUsedCards.add(new FriendshipIsMagicTMCard());
		listOfUsedCards.add(new EstateCard());
		listOfUsedCards.add(new GoldCard());
		listOfUsedCards.add(new SilverCard());
		listOfUsedCards.add(new CopperCard());
		listOfUsedCards.add(new CurseCard());
		
		supply = new Supply(listOfUsedCards, NUM_PLAYERS);
		supply.setBounds(0,0,HAND_X_START - 30,640);
		add(supply);
	}
	
	//Where the cards to enter the list are added
	private void initializeCardList() 
	{
		listOfAllCards.clear();
		//here is a massive pile of initialization
		listOfAllCards.add(new BoutiqueCard());
		listOfAllCards.add(new CloudsdaleCard());
		listOfAllCards.add(new CanterlotCard());
		listOfAllCards.add(new CutieMarkCard());
		listOfAllCards.add(new DearPrincessCelestiaCard());
		listOfAllCards.add(new DiggerCard());
		listOfAllCards.add(new EarthPonyCard());
		listOfAllCards.add(new ElementsOfHarmonyCard());
		listOfAllCards.add(new FruitPunchCard());
		listOfAllCards.add(new GrandGallopingGalaCard());
		listOfAllCards.add(new IronPonyCompetitionCard());
		listOfAllCards.add(new JewelsCard());
		listOfAllCards.add(new ParaspriteCard());
		listOfAllCards.add(new PegasusCard());
		listOfAllCards.add(new PonyvilleCard());
		listOfAllCards.add(new RebirthCard());
		listOfAllCards.add(new SafeCard());
		listOfAllCards.add(new SatchelCard());
		listOfAllCards.add(new ScienceCard());
		listOfAllCards.add(new ShowboaterCard());
		listOfAllCards.add(new StewCard());
		listOfAllCards.add(new SweetAppleAcresCard());
		listOfAllCards.add(new UnicornCard());
		listOfAllCards.add(new WeatherFactoryCard());
		listOfAllCards.add(new WorkhorseCard());
	}
	
	private void initializePlayers()
	{
		players = new Player[NUM_PLAYERS];
		
		for (int i = 0; i < players.length; i++)
		{
			//Initialize array, set current Player
			players[i] = new Player("Player " + Integer.toString(i + 1));
			//Inform of supply.
			//REORGANIZE THIS WHEN IT WORKS
			players[i].setSupply(supply);
			players[i].setGameInterface(this);
			supply.setCurrentPlayer(players[i]);
			//Then have that player gain the starting deck and draw the starting hand
			for (int j = 0; j < 7; j++)
			{
				players[i].gainCard(new CopperCard());
			}
			for (int j = 0; j < 3; j++)
			{
				players[i].gainCard(new EstateCard());
			}
			players[i].drawCards(5);
		}
		
		//Inform players of existence of all players after initializing
		//And create a Radio Button for each player
		for (int i = 0; i < players.length; i++)
		{
			players[i].setPlayers(players);
		}
		players[0].setIsThisPlayersTurn(true);
		
		playerPanel = new PlayerViewer(players);
		playerPanel.setInterface(this);
		playerPanel.setBounds(PLAYER_VIEW_X, PLAYER_VIEW_Y, 100, 32 * NUM_PLAYERS);
		currentPlayerIndex = 0;
		displayedPlayerIndex = 0;
		supply.setCurrentPlayer(players[currentPlayerIndex]);
		supply.setDisplayedPlayer(players[displayedPlayerIndex]);
	}
	
	public void paint(Graphics g)
	{
		//		USE getContentPane() !!!!
		this.getContentPane().removeAll();
		g.clearRect(PLAYER_INFO_X_START,PLAYER_INFO_Y_START,APPLET_WIDTH-PLAYER_INFO_X_START,APPLET_HEIGHT-PLAYER_INFO_Y_START);
		drawButtons();
		drawSupply();
		drawHand();
		players[currentPlayerIndex].showBuyable();
		drawPlayerInfo();
		drawTextLog();
		//setOptionSelector(new OptionSelector(2, players[currentPlayerIndex].getHand()));
		//select.setOptions("1","2","3");
		add(select);
		select.repaint();
		players[currentPlayerIndex].setAllowedToAct(!select.isVisible());
	}
	
	private void drawTextLog() 
	{
		text.setText("");
		for (int i = 0; i < TextLogger.TEXT_LOG.size(); i++)
		{
			text.append(TextLogger.TEXT_LOG.get(i) + "\n");
		}
		add(textDisplay);
		textDisplay.repaint();
	}
	
	private void drawPlayerInfo() 
	{
		JLabel actions = new JLabel("Actions: " + players[displayedPlayerIndex].getActions());
		JLabel buys = new JLabel("Buys: " + players[displayedPlayerIndex].getBuys());
		JLabel money = new JLabel("Money: $" + players[displayedPlayerIndex].getMoney());
		JLabel friendship = new JLabel("Friendship: " + players[displayedPlayerIndex].getFriendship());
		
		String d = "";
		for (int i = 0; i < players[displayedPlayerIndex].getCardsInDeck(); i++)
		{
			d += "|";
		}
		JLabel deck = new JLabel("Deck: " + d);
		
		d = "";
		for (int i = 0; i < players[displayedPlayerIndex].getCardsInDiscard(); i++)
		{
			d += "|";
		}
		JLabel discard = new JLabel("Discard: " + d);
		
		actions.setBounds(PLAYER_INFO_X_START,PLAYER_INFO_Y_START,100,12);
		buys.setBounds(PLAYER_INFO_X_START,PLAYER_INFO_Y_START + 15, 100,12);
		money.setBounds(PLAYER_INFO_X_START,PLAYER_INFO_Y_START+ 30,100,12);
		friendship.setBounds(PLAYER_INFO_X_START, PLAYER_INFO_Y_START + 45, 100, 12);
		deck.setBounds(PLAYER_INFO_X_START, PLAYER_INFO_Y_START + 60, 40 + players[displayedPlayerIndex].getCardsInDeck() * 5, 12);
		discard.setBounds(PLAYER_INFO_X_START, PLAYER_INFO_Y_START + 75, 60 + players[displayedPlayerIndex].getCardsInDiscard() * 5, 12);
		
		add(actions);
		actions.repaint();
		
		add(buys);
		buys.repaint();
		
		add(money);
		money.repaint();
		
		add(friendship);
		friendship.repaint();
		
		add(deck);
		deck.repaint();
		
		add(discard);
		discard.repaint();
	}
	
	private void drawSupply() 
	{
		add(supply);
		supply.repaint();
	}
	
	private void drawButtons() 
	{
		draw.setVisible(currentPlayerIndex == displayedPlayerIndex);
		add(draw);
		draw.repaint();
		
		playTreasure.setVisible(currentPlayerIndex == displayedPlayerIndex);
		playTreasure.calculateText();
		add(playTreasure);
		playTreasure.repaint();
		
		add(playerPanel);
		playerPanel.repaint();
		
		add(restart);
		restart.repaint();
		
		add(info);
		info.repaint();
		
		add(viewDeck);
		viewDeck.repaint();
	}
	
	private void drawHand() 
	{
		cardHolder.removeAll();
		int xOffset = 0;
		int yOffset = 0;
		Collections.sort(players[displayedPlayerIndex].getHand(), SORT_FOR_USEFULNESS);
		ArrayList<DomCard> handToDraw = players[displayedPlayerIndex].getHand();
		DomCardComponent cardToDraw;
		for (int i = 0; i < handToDraw.size(); i++)
		{
			cardToDraw = new DomCardComponent(handToDraw.get(i));
			
			if (xOffset + cardToDraw.getDisplayLength() > HAND_X_SIZE)
			{
				yOffset += 24;
				xOffset = 0;
			}
			
			cardToDraw.setBounds(xOffset, yOffset, cardToDraw.getDisplayLength(),19);
			
			xOffset = xOffset + cardToDraw.getDisplayLength() + 5;
			
			if (yOffset > cardHolder.getHeight())
			{
				cardHolder.setPreferredSize(new Dimension(HAND_X_SIZE, yOffset + 19));
			}
			cardHolder.add(cardToDraw);
			cardToDraw.repaint();
			cardHolder.revalidate();
		}
		add(cardScroll);
		cardScroll.revalidate();
		cardScroll.repaint();
	}
	
	public void mouseClicked(MouseEvent arg0)
	{
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private class DrawButton extends JButton implements ActionListener 
	{
		public DrawButton() 
		{
			super("end turn");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) 
		{
			/**
			 * This follows Isotropic convention as closely as possible
			 * Still some kinks in this area of the code to work out
			 * 
			 * An end turn is called into question when
			 * 1. Action phase, actions in hand, at least 1 action
			 * 2. At least 1 buy left, player has bought at least one card this turn.
			 */
			
			if (getText().equals("?!"))
			{
				endCurrentTurn();
			}
			else if ((players[currentPlayerIndex].getBuys() > 0) && 
					!players[currentPlayerIndex].hasBoughtCardThisTurn())
			{
				setText("?!");
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if ((players[currentPlayerIndex].getPhase().equals("Action")) &&
					(players[currentPlayerIndex].searchZone(players[currentPlayerIndex].getHand(), "Action").size() > 0) &&
					(players[currentPlayerIndex].getActions() > 0))
			{
				setText("?!");
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else endCurrentTurn();
		}
	}
	
	public void endCurrentTurn() 
	{
		players[currentPlayerIndex].endTurn();
		repaint();
	}
	
	private class MoneyButton extends JButton implements ActionListener
	{
		public MoneyButton() 
		{
			super("+$");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) 
		{
			//questionable times to play money
			//1. Actions in hand with actions leftover in action phase
			
			if (getText().equals("?!"))
			{
				playAllTreasure();
			}
			else if ((players[currentPlayerIndex].getPhase().equals("Action")) &&
					(players[currentPlayerIndex].searchZone(players[currentPlayerIndex].getHand(), "Action").size() > 0) &&
					(players[currentPlayerIndex].getActions() > 0))
			{
				setText("?!");
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else playAllTreasure();
		}
		
		public void calculateText()
		{
			int copperCount = 0;
			int silverCount = 0;
			int goldCount = 0;
			DomCard card;
			
			for (int i = 0; i < players[currentPlayerIndex].getCardsInHand(); i++)
			{
				card = players[currentPlayerIndex].getHand().get(i);
				if (card.getCardName().equals("Copper"))
				{
					copperCount++;
				}
				else if (card.getCardName().equals("Silver"))
				{
					silverCount++;
				}
				else if (card.getCardName().equals("Gold"))
				{
					goldCount++;
				}
			}
			int moneyInHand = 0;
			moneyInHand += copperCount * players[currentPlayerIndex].getCopperValue();
			moneyInHand += silverCount * 2;
			moneyInHand += goldCount * 3;
			
			if (moneyInHand == 0)
			{
				setVisible(false);
			}
			else
			{
				setText("+$" + Integer.toString(moneyInHand));
			}
		}
	}
	
	/**
	 * Plays all treasure in you hand that can be played optimally in all situations
	 * In this set, it will only be basic treasures
	 */
	public void playAllTreasure()
	{
		DomCard card;
		//Removing from the list part way through loop leads to errors, so we store the cards to be removed
		ArrayList<DomCard> markedForRemoval = new ArrayList<DomCard>();
		
		if (!players[currentPlayerIndex].hasBoughtCardThisTurn())
		{
			for (int i = 0; i < players[currentPlayerIndex].getCardsInHand(); i++)
			{
				card = players[currentPlayerIndex].getHand().get(i);
				if ((card.getCardName().equals("Copper")) || (card.getCardName().equals("Silver")) || (card.getCardName().equals("Gold")))
				{
					card.play();
					markedForRemoval.add(card);
				}
			}
			DomCardComponent.doPlayAllTreasureEvents(markedForRemoval);
			repaint();
		}
	}
	
	/**
	 * Useful because I suck at coding in a non-convoluted way
	 * @return The supply for this game
	 */
	public Supply getSupply()
	{
		return supply;
	}
	
	/**
	 * Sets the displayed option selector.
	 * Allows players to prompt the GUI
	 * @param newSelector The option selector to use
	 */
	public void setOptionSelector(OptionSelector newSelector)
	{
		select = newSelector;
		//System.out.println(select.getBounds());
		select.setBounds(OPTION_X_START,OPTION_Y_START,OPTION_WIDTH,OPTION_HEIGHT);
		select.repaint();
	}
	
	public OptionSelector getOptionSelector()
	{
		return select;
	}
	
	/*
	 public void setAllowedToTakeAction(boolean ability)
	 {
	 allowedToTakeAction = ability;
	 }
	 
	 public boolean getAllowedToTakeAction()
	 {
	 return allowedToTakeAction;
	 }
	 */
	
	public Frame findParentFrame()
	{ 
		Container c = this; 
		while(c != null)
		{ 
			if (c instanceof Frame) 
				return (Frame) c; 
			
			c = c.getParent(); 
		} 
		return (Frame) null; 
	} 
	
	public ArrayList<DomCard> getUsedCards()
	{
		return listOfUsedCards;
	}
	
	public static DomCard lookUpCard(String name)
	{		
		for (int i = 0; i < listOfUsedCards.size(); i++)
		{
			if (listOfUsedCards.get(i).getCardName().equals(name))
			{
				return listOfUsedCards.get(i);
			}
		}
		return null;
	}
	
	public boolean setDisplayedPlayer(int i)
	{
		//handles events important to making game function correctly
		displayedPlayerIndex = i;
		supply.setDisplayedPlayer(players[i]);
		
		//handles making the graphics look nicer
		Enumeration<AbstractButton> buttons = playerPanel.playerView.getElements();
		
		while (buttons.hasMoreElements())
		{
			JRadioButton b = (JRadioButton) buttons.nextElement();
			if (b.getActionCommand().equals(i + ""))
			{
				b.setSelected(true);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns index of current player
	 */
	public int getCurrentPlayer()
	{
		return currentPlayerIndex;
	}
	
	/**
	 * The usefulness of a card is defined in order down the list by
	 * 1. Actions > Treasures > Victory > Curse
	 * 2. Higher Cost
	 * 3. Alphabetically
	 * 
	 * More useful cards lie towards the left of the hand.
	 * @author alexI
	 *
	 */
	private class UsefulComparator implements Comparator<DomCard>
	{
		public int compare(DomCard arg0, DomCard arg1) 
		{
			if (arg0.isType("Action"))
			{
				if (!(arg1.isType("Action")))
				{
					return -1;
				}
				else
				{
					return -arg0.compareTo(arg1);
				}
			}
			else if (arg1.isType("Action"))
			{
				return 1;
			}
			else if (arg0.isType("Treasure"))
			{
				if (!(arg1.isType("Treasure")))
				{
					return -1;
				}
				else
				{
					return -arg0.compareTo(arg1);
				}
			}
			else if (arg1.isType("Treasure"))
			{
				return 1;
			}
			else if (arg0.isType("Victory"))
			{
				if (!(arg1.isType("Victory")))
				{
					return -1;
				}
				else
				{
					return -arg0.compareTo(arg1);
				}
			}
			else if (arg1.isType("Victory"))
			{
				return 1;
			}
			else if (arg0.isType("Curse"))
			{
				if (!(arg1.isType("Curse")))
				{
					return -1;
				}
				else
				{
					return -arg0.compareTo(arg1);
				}
			}
			else return 1;
		}
	}
	
	public void startNextPlayerTurn() 
	{
		currentPlayerIndex++;
		draw.setText("end turn");
		playTreasure.setText("+$");
		
		if (currentPlayerIndex == NUM_PLAYERS)
		{
			currentPlayerIndex = 0;
		}
		setDisplayedPlayer(currentPlayerIndex);
		players[currentPlayerIndex].startTurn();
		//displayedPlayerIndex = currentPlayerIndex;
		supply.setCurrentPlayer(players[currentPlayerIndex]);

		repaint();
	}
	
	public void concludeGame() 
	{
		ArrayList<String> results = new ArrayList<String>();
		
		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			results.add(players[i].getName() + " has earned " + players[i].getTotalVP() + " VP");
		}
		
		JOptionPane.showMessageDialog(findParentFrame(), results);
		TextLogger.TEXT_LOG.addAll(results);
		viewDeck.setVisible(true);
		/*
		 JOptionPane resultBox = new JOptionPane(results, JOptionPane.PLAIN_MESSAGE);
		 JDialog d = new JDialog(findParentFrame(), true);
		 d.setContentPane(resultBox);
		 d.setDefaultCloseOperation(d.DISPOSE_ON_CLOSE);
		 d.pack();
		 d.setVisible(true);
		 */
	}
	
	/**
	 * Gives information on all the cards owned by each player
	 *
	 */
	public void viewDeckContents()
	{
		String log;
		ArrayList<DomCard> cards;
		
		//find deck contents, use to make Strings that hold the info
		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			log = "Player " + (i+1) + ":";
			cards = players[i].getAllCardsOwned();
			Collections.sort(cards, SORT_FOR_USEFULNESS);
			
			log += DomCard.findCountsOfContainedCards(cards);
			TextLogger.record(log);
		}
		
		repaint();
	}
	
	public void newGame()
	{
		init();
		repaint();
	}
	
	private class RestartButton extends JButton implements ActionListener
	{
		public RestartButton() 
		{
			super("New Game");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) 
		{
			Object[] options = {"Yes", "No"};
			
			int n = JOptionPane.showOptionDialog(findParentFrame(),
					"Are you sure you want to restart?",
					"Restart Game",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[1]);
			
			if (n == JOptionPane.YES_OPTION)
			{
				newGame();
			}
		}
	}
	
	public void resetText()
	{
		playTreasure.calculateText();
		supply.resetText();
	}
	
	public void info()
	{
		//COPY PASTE INTO TXT FILE
		//Current player info: # cards in deck.
		//Noncurrent: Shape corresponding to deck (or could be lazy for now)
		//Free Info: Cards in trash, cards in hand
		
		ArrayList<String> output = new ArrayList<String>();
		
		String trash = "Trash: " + DomCard.findCountsOfContainedCards(Player.trash);
		
		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			if (displayedPlayerIndex == i)
				addInfo(players[i], output, true);
			else
				addInfo(players[i], output, false);
		}
		
		InfoDialog id = new InfoDialog(findParentFrame(), output);
		id.pack();
		id.setLocation(200,200);
		id.setVisible(true);
	}
	
	private void addInfo(Player player, ArrayList<String> output, boolean displayed) 
	{
		output.add("");
		output.add("--" + player.getName() + "--");
		
		if (displayed)
		{
			String hand = "Hand: " + DomCard.findCountsOfContainedCards(player.getHand());
			output.add(hand);
		}
		else
			output.add("Hand: " + player.getCardsInHand() + " cards");
		
		String play = "Play: " + DomCard.findCountsOfContainedCards(player.getPlay());
		output.add(play);
		
		if (displayed)
			output.add("Deck: " + player.getCardsInDeck() + " cards");
		else
		{
			String deck = "Deck: ";
			for (int i = 0; i < player.getCardsInDeck(); i++)
			{
				deck += "|";
			}
			output.add(deck);
		}
		
		String discard = "Discard: ";
		for (int i = 0; i < player.getCardsInDiscard(); i++)
		{
			discard += "|";
		}
		output.add(discard);
		
		output.add("Friendship: " + player.getFriendship());
	}
	
	private class InfoButton extends JButton implements ActionListener
	{
		public InfoButton() 
		{
			super("Info");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) 
		{
			info();
		}
	}
	
	private class DeckButton extends JButton implements ActionListener
	{
		public DeckButton()
		{
			super("View Decks");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) 
		{
			viewDeckContents();
		}
	}
}
