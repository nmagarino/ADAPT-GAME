import java.awt.Color;
import java.awt.Font;
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
	
	private static final int cDrawRadius = 100;
	
	public CombatDisplay(Creature c1, Creature c2, GameCourt gc) {
		creature1 = c1;
		creature2 = c2;
		frame = 0;
		court = gc;
		
		c1X = (int) (court.COURT_WIDTH/5.0f * 1.0f) - cDrawRadius/2;
		c2X = (int) (court.COURT_WIDTH/5.0f * 4.0f) - cDrawRadius/2;
		c1Y = court.COURT_HEIGHT + cDrawRadius/2;
		c2Y = court.COURT_HEIGHT + cDrawRadius/2;
		c1NumX = (int) (court.COURT_WIDTH/5.0f * 1.0f) - cDrawRadius/2 + 40;
		c2NumX = (int) (court.COURT_WIDTH/5.0f * 4.0f) - cDrawRadius/2 + 40;
		c1NumY = court.COURT_HEIGHT + 100;
		c2NumY = court.COURT_HEIGHT + 100;
		
		c1Num = creature1.getCombat();
		c2Num = creature2.getCombat();
	}
	
	public void update() {
		if (frame > 80) {
			court.stopFight();
		}
		double riseAnimLength = 8.0;
		double pause1Length = 5.0;
		double displayNumbersLength = 8.0;
		double pause2Length = 15.0;
		double attackAnimLength = 8.0;
		double dieAnimLength = 5.0;
		double pause3Length = 5.0;
		double leaveAnimLength = 8.0;
		if (frame <= riseAnimLength) {
			c1Y = (int) ((court.COURT_HEIGHT + cDrawRadius/2) * (1.0 - frame/riseAnimLength) + (court.COURT_HEIGHT/2 - cDrawRadius/2) * (frame/riseAnimLength));
			c2Y = (int) ((court.COURT_HEIGHT + cDrawRadius/2) * (1.0 - frame/riseAnimLength) + (court.COURT_HEIGHT/2 - cDrawRadius/2) * (frame/riseAnimLength));
		}
		else if (frame <= riseAnimLength + pause1Length) {
			
		}
		else if (frame <= riseAnimLength + pause1Length + displayNumbersLength) {
			double animFrac = (frame - (riseAnimLength + pause1Length))/displayNumbersLength;
			c1NumY = (int) ((c1Y + cDrawRadius/2) * (1.0 - animFrac) + (c1Y - 50) * (animFrac));
			c2NumY = (int) ((c2Y + cDrawRadius/2) * (1.0 - animFrac) + (c2Y - 50) * (animFrac));
		}
		else if (frame <= riseAnimLength + pause1Length + displayNumbersLength + pause2Length) {
			
		}
		else if (frame <= riseAnimLength + pause1Length + displayNumbersLength + pause2Length + attackAnimLength) {
			double animFrac = (frame - (riseAnimLength + pause1Length + displayNumbersLength + pause2Length))/attackAnimLength;
			double position = Math.pow(1.26 * animFrac, 3) - Math.pow(animFrac, 2);
			c1X = c1NumX = (int) ((court.COURT_WIDTH/5.0f * 1.0f) - cDrawRadius/2 + position * 300);
		}
		else if (frame <= riseAnimLength + pause1Length + displayNumbersLength + pause2Length + attackAnimLength + dieAnimLength) {
			double animFrac = (frame - (riseAnimLength + pause1Length + displayNumbersLength + pause2Length + attackAnimLength))/dieAnimLength;
			c2X = c2NumX = (int) ((court.COURT_WIDTH/5.0f * 4.0f) - cDrawRadius/2 + animFrac * 300);
		}
		else if (frame <= riseAnimLength + pause1Length + displayNumbersLength + pause2Length + attackAnimLength + dieAnimLength + pause3Length) {
			
		}
		else if (frame <= riseAnimLength + pause1Length + displayNumbersLength + pause2Length + attackAnimLength + dieAnimLength + pause3Length + leaveAnimLength) {
			double animFrac = (frame - (riseAnimLength + pause1Length + displayNumbersLength + pause2Length + attackAnimLength + dieAnimLength + pause3Length))/leaveAnimLength;
			c1Y = c1NumY = (int) ((court.COURT_HEIGHT + cDrawRadius/2) * (animFrac) + (court.COURT_HEIGHT/2 - cDrawRadius/2) * (1.0 - animFrac));
		}
		
		frame++;
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, court.COURT_WIDTH, court.COURT_HEIGHT);
		g.setColor(new Color(0, 0, 0, 255));
		
		g.setFont(new Font("Helvetica", Font.PLAIN, 60));
		
		g.setColor(Color.WHITE);
		g.drawString(c1Num + "", c1NumX, c1NumY);
		g.drawString(c2Num + "", c2NumX, c2NumY);

		g.setColor(Color.BLACK);
		g.fillOval(c1X, c1Y, cDrawRadius, cDrawRadius);
		g.fillOval(c2X, c2Y, cDrawRadius, cDrawRadius);
		
		g.setColor(creature1.color);
		g.fillOval(c1X + 4, c1Y + 4, cDrawRadius - 8, cDrawRadius - 8);
		g.setColor(creature2.color);
		g.fillOval(c2X + 4, c2Y + 4, cDrawRadius - 8, cDrawRadius - 8);
	}
}
