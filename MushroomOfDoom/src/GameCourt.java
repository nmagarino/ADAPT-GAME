/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
    public boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    
    public int whosTurn = 0;	//0 is boards turn (events, AI creatures)
    
    public GameBoard board;
    public Player[] players;
    
    private Map<BoardTile, CreaturePath> validPaths;
    
    public TraitDeck traitDeck;
    public TraitDisplay[] traitDisplays;
    public TraitDisplay traitToAddDisplay;
    public Creature displayingCreature;
    public boolean displayingEvolving;
    public int traitToReplace;
    public boolean userPickReplaceTrait;

    // Game constants
    public static final int BOARD_DIMS = 40;

    public static final int COURT_WIDTH = 600;
    public static final int COURT_HEIGHT = 600;
    
    int frame;
    boolean isAnimating;
    int evolveAnimTick;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
            	
            }

            public void keyReleased(KeyEvent e) {
                
            }
        });
        
        addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		int x = e.getX();
        		int y = e.getY();
        		int spaceX = x / (GameCourt.COURT_WIDTH/GameCourt.BOARD_DIMS);
        		int spaceY = y / (GameCourt.COURT_HEIGHT/GameCourt.BOARD_DIMS);
        		
        		if (!displayingEvolving) {
	        		Player currPlayer = players[whosTurn - 1];
	        		CreaturePath path = validPaths.get(board.board[spaceX][spaceY]);
	        		if (path != null) {
	        			animateCreatureMovement(currPlayer, path);
		        		currPlayer.moveCreatureToTile(spaceX, spaceY);
		        		incrementTurn();
	        		}
        		}
        		else if (userPickReplaceTrait && evolveAnimTick == 28) {
        			for (int i = 0; i < traitDisplays.length; i++) {
        				TraitDisplay disp = traitDisplays[i];
        				if (x > disp.getPx() && x < disp.getPx() + disp.getWidth()) {
        					 if (y > disp.getPy() && y < disp.getPy() + disp.getHeight()) {
        						 traitToReplace = i;
        						 disp.scaleDes = 0;
        						 evolveAnimTick++;
        					 }
        				}
        			}
        		}
        	}
        });
        
        addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
        		int y = e.getY();
        		int spaceX = x / (GameCourt.COURT_WIDTH/GameCourt.BOARD_DIMS);
        		int spaceY = y / (GameCourt.COURT_HEIGHT/GameCourt.BOARD_DIMS);
        		
        		Creature c = mouseOverCreature(x, y);
        		if (c != null && !displayingEvolving) {
        			displayCreatureTraits(c);
        		}
        		else if (!displayingEvolving) {
        			stopDisplayCreatureTraits();
        		}
				
				Collection<CreaturePath> paths = validPaths.values();
		        for (CreaturePath path : paths) {
		        	path.mouseOver = false;
		        }
		        
		        if (spaceX >= BOARD_DIMS || spaceY >= BOARD_DIMS) return;
        		CreaturePath path = validPaths.get(board.board[spaceX][spaceY]);
        		if (path != null) {
        			path.mouseOver = true;
        		}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				
			}
		});

        this.status = status;
        
        this.board = new GameBoard(BOARD_DIMS, BOARD_DIMS);
        traitDeck = new TraitDeck();
        
        validPaths = new HashMap<BoardTile, CreaturePath>();
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        
        board.readMap();
        players = new Player[board.startTiles.size()];
        for (int i = 0; i < board.startTiles.size(); i++) {
        	BoardTile start = board.startTiles.get(i);
        	players[i] = new Player(start.spaceX, start.spaceY, this);
        	System.out.println("New player");
        }
        
        traitDisplays = new TraitDisplay[3];

        playing = true;
        status.setText("Running...");
        frame = 0;
        evolveAnimTick = 0;
        isAnimating = false;
        whosTurn = 0;

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        
        incrementTurn();
    }
    


    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
            for (int i = 0; i < players.length; i++) {
            	players[i].update();
            }
            
            for (int i = 0; i < traitDisplays.length; i++) {
            	if (traitDisplays[i] != null) traitDisplays[i].update();
            }
            if (traitToAddDisplay != null) traitToAddDisplay.update();
            
            if (displayingEvolving) {
            	if (!(this.userPickReplaceTrait && evolveAnimTick == 28)) evolveAnimTick++;
            	if (evolveAnimTick == 10) {
                	traitToAddDisplay.Ydes = 300;
            	}
            	if (evolveAnimTick == 30) {
                	traitToAddDisplay.Xdes = traitDisplays[traitToReplace].getPx();
                	traitToAddDisplay.Ydes = traitDisplays[traitToReplace].getPy();
            	}
            	if (evolveAnimTick == 50) {
            		this.displayingCreature.traits[traitToReplace] = traitToAddDisplay.trait;
            		stopDisplayCreatureTraits();
            		evolveAnimTick = 0;
            		this.displayingEvolving = false;
            	}
            }
            
            frame++;

            // update the display
            repaint();
        }
    }
    
    public void animateCreatureMovement(Creature creature, CreaturePath path) {
    	creature.animateAlongPath(path);
    	isAnimating = true;
    }
    
    public void displayCreatureTraits(Creature c) {
    	if (displayingCreature == c) return;
    	displayingCreature = c;
    	for (int i = 0; i < c.traits.length; i++) {
    		traitDisplays[i] = new TraitDisplay(c.traits[i], this);
    		traitDisplays[i].setPx((int) (c.getPx() + c.getWidth()/2.0));
    		traitDisplays[i].setPy((int) (c.getPy() + c.getHeight()/2.0));
    		traitDisplays[i].scale = 0f;
    		traitDisplays[i].Xdes = COURT_WIDTH/c.traits.length * i + (COURT_WIDTH - (TraitDisplay.defaultWidth * c.traits.length))/(2 * c.traits.length);
    		traitDisplays[i].Ydes = 20;
    		traitDisplays[i].scaleDes = 1f;
    	}
    }
    
    public void stopDisplayCreatureTraits() {
    	if (traitDisplays[0] == null) return;
    	if (displayingCreature == null) return;
    	for (int i = 0; i < displayingCreature.traits.length; i++) {
    		traitDisplays[i].Xdes = (int) (displayingCreature.getPx() + displayingCreature.getWidth()/2.0);
    		traitDisplays[i].Ydes = (int) (displayingCreature.getPy() + displayingCreature.getHeight()/2.0);
    		traitDisplays[i].scaleDes = 0f;
    	}
    	if (traitToAddDisplay != null) {
    		traitToAddDisplay.Xdes = (int) (displayingCreature.getPx() + displayingCreature.getWidth()/2.0);
    		traitToAddDisplay.Ydes = (int) (displayingCreature.getPy() + displayingCreature.getHeight()/2.0);
    		traitToAddDisplay.scaleDes = 0f;
    	}
    	
    	displayingCreature = null;
    }
    
    public void displayEvolving(Creature c, Trait t, int whichTrait, boolean userPick) {
    	displayingEvolving = true;
    	evolveAnimTick = 0;
    	this.traitToReplace = whichTrait;
    	this.userPickReplaceTrait = userPick;
    	displayCreatureTraits(c);
    	traitToAddDisplay = new TraitDisplay(t, this);
    	traitToAddDisplay.setPx(COURT_WIDTH/2 - traitToAddDisplay.getWidth()/2);
    	traitToAddDisplay.setPy(600);
    	traitToAddDisplay.scale = 1f;
    	traitToAddDisplay.Xdes = COURT_WIDTH/2 - traitToAddDisplay.getWidth()/2;
    	traitToAddDisplay.Ydes = 600;
    	traitToAddDisplay.scaleDes = 1f;
    }
    
    public void stopAnimating() {
    	isAnimating = false;
    }
    
    public void incrementTurn() {
    	whosTurn++;
    	if (whosTurn > players.length) whosTurn = 1;
    	validPaths = players[whosTurn - 1].getPotentialPaths();
    }
    
    public Creature mouseOverCreature(int x, int y) {
    	for (Creature p : players) {
    		if (x > p.getPx() && x < p.getPx() + p.getWidth()) {
        		if (y > p.getPy() && y < p.getPy() + p.getHeight()) {
        			return p;
        		}
    		}
    	}
    	return null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        board.draw(g);
        if (validPaths != null && !displayingEvolving) {
	        Collection<CreaturePath> paths = validPaths.values();
	        for (CreaturePath path : paths) {
	        	path.draw(g, frame);
	        }
        }
        
        for (int i = 0; i < players.length; i++) {
        	players[i].draw(g);
        }
        
        for (int i = 0; i < traitDisplays.length; i++) {
        	if (traitDisplays[i] != null) traitDisplays[i].draw(g);
        }
        if (traitToAddDisplay != null) traitToAddDisplay.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}