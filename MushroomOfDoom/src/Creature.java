import java.awt.Color;
import java.awt.Graphics;

public abstract class Creature extends GameObj{
	int spaceX, spaceY;
	Trait[] traits;
	boolean hasLegs;
	
	public Creature(int px, int py) {
		super(0, 0, px * (GameCourt.COURT_WIDTH/GameCourt.BOARD_DIMS), py * (GameCourt.COURT_HEIGHT/GameCourt.BOARD_DIMS), 1, 1, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		spaceX = px;
		spaceY = py;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
        g.fillOval(
        		spaceX * (GameCourt.COURT_WIDTH/GameCourt.BOARD_DIMS),
        		spaceY * (GameCourt.COURT_HEIGHT/GameCourt.BOARD_DIMS),
        		15, 15);
	}
}
