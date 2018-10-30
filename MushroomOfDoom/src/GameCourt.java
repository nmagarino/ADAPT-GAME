/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
    private Square square; // the Black Square, keyboard control
    private Circle snitch; // the Golden Snitch, bounces
    private Poison poison; // the Poison Mushroom, doesn't move

    public boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    
    public int whosTurn = 0;	//0 is boards turn (events, AI creatures)
    
    public GameBoard board;
    public Player[] players;
    
    private Map<BoardTile, CreaturePath> validPaths;

    // Game constants
    public static final int BOARD_DIMS = 40;

    public static final int COURT_WIDTH = 600;
    public static final int COURT_HEIGHT = 600;
    public static final int SQUARE_VELOCITY = 4;
    
    int frame;
    boolean isAnimating;

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
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    square.setVx(-SQUARE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    square.setVx(SQUARE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    square.setVy(SQUARE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    square.setVy(-SQUARE_VELOCITY);
                }
            }

            public void keyReleased(KeyEvent e) {
                square.setVx(0);
                square.setVy(0);
            }
        });
        
        addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		int x = e.getX();
        		int y = e.getY();
        		x = x / (GameCourt.COURT_WIDTH/GameCourt.BOARD_DIMS);
        		y = y / (GameCourt.COURT_HEIGHT/GameCourt.BOARD_DIMS);
        		Player currPlayer = players[whosTurn - 1];
        		CreaturePath path = validPaths.get(board.board[x][y]);
        		if (path != null) {
        			animateCreatureMovement(currPlayer, path);
	        		currPlayer.spaceX = x;
	        		currPlayer.spaceY = y;
	        		incrementTurn();
        		}
        	}
        });
        
        addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Collection<CreaturePath> paths = validPaths.values();
		        for (CreaturePath path : paths) {
		        	path.mouseOver = false;
		        }
				int x = e.getX();
        		int y = e.getY();
        		x = x / (GameCourt.COURT_WIDTH/GameCourt.BOARD_DIMS);
        		y = y / (GameCourt.COURT_HEIGHT/GameCourt.BOARD_DIMS);
        		CreaturePath path = validPaths.get(board.board[x][y]);
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
        validPaths = new HashMap<BoardTile, CreaturePath>();
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        square = new Square(COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
        poison = new Poison(COURT_WIDTH, COURT_HEIGHT);
        snitch = new Circle(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW);
        
        board.readMap();
        players = new Player[board.startTiles.size()];
        for (int i = 0; i < board.startTiles.size(); i++) {
        	BoardTile start = board.startTiles.get(i);
        	players[i] = new Player(start.spaceX, start.spaceY, this);
        	System.out.println("New player");
        }

        playing = true;
        status.setText("Running...");
        frame = 0;
        isAnimating = false;

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
            
            frame++;

            // update the display
            repaint();
        }
    }
    
    public void animateCreatureMovement(Creature creature, CreaturePath path) {
    	creature.animateAlongPath(path);
    	isAnimating = true;
    }
    
    public void stopAnimating() {
    	isAnimating = false;
    }
    
    public void incrementTurn() {
    	whosTurn++;
    	if (whosTurn > players.length) whosTurn = 1;
    	validPaths = players[whosTurn - 1].getPotentialPaths();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        /*square.draw(g);
        poison.draw(g);
        snitch.draw(g);*/
        
        board.draw(g);
        if (validPaths != null) {
	        Collection<CreaturePath> paths = validPaths.values();
	        for (CreaturePath path : paths) {
	        	path.draw(g, frame);
	        }
        }
        
        for (int i = 0; i < players.length; i++) {
        	players[i].draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}