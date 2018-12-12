import java.awt.Color;
import java.awt.Graphics;

public class CombatDisplay {
	public Creature creature1;
	public Creature creature2;
	
	private int frame;
	private GameCourt court;
	
	private int c1X, c1Y;
	private int c2X, c2Y;
	private int c1NumX, c1NumY;
	private int c2NumX, c2NumY;
	
	private int c1Num;
	private int c2Num;
	
	private int cDrawRadius;
	
	public CombatDisplay(Creature c1, Creature c2, GameCourt gc) {
		creature1 = c1;
		creature2 = c2;
		frame = 0;
		court = gc;
		
		c1X = c1X = (int) (court.COURT_WIDTH/5.0f * 1.0f);
		c2X = c2X = (int) (court.COURT_WIDTH/5.0f * 4.0f);
		c1Y = court.COURT_HEIGHT + cDrawRadius;
		c2Y = court.COURT_HEIGHT + cDrawRadius;
		
		c1Num = creature1.getCombat();
		c2Num = creature2.getCombat();
		
		cDrawRadius = 100;
	}
	
	public void update() {
		if (frame > 100) {
			court.stopFight();
		}
		double riseAnimLength = 10.0;
		if (frame < riseAnimLength) {
			c1Y = (int) ((court.COURT_HEIGHT + cDrawRadius) * (1.0 - frame/riseAnimLength) + (court.COURT_HEIGHT/2) * (frame/riseAnimLength));
			c2Y = (int) ((court.COURT_HEIGHT + cDrawRadius) * (1.0 - frame/riseAnimLength) + (court.COURT_HEIGHT/2) * (frame/riseAnimLength));
		}
		
		frame++;
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 70));
		g.fillRect(0, 0, court.COURT_WIDTH, court.COURT_HEIGHT);
		g.setColor(new Color(0, 0, 0, 255));

		g.setColor(creature1.color);
		g.fillOval(c1X, c1Y, cDrawRadius, cDrawRadius);
		g.setColor(creature2.color);
		g.fillOval(c2X, c2Y, cDrawRadius, cDrawRadius);
	}
}
