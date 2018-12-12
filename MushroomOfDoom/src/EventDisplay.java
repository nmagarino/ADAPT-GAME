import java.awt.Graphics;

public class EventDisplay {
	private Event e;
	private CardDisplay cd;

	private int frame;
	private GameCourt court;
	
	public EventDisplay(Event e, GameCourt gc) {
		frame = 0;
		court = gc;
		this.e = e;
		
		cd = new CardDisplay(e, gc);
	}
	
	public void update() {
		if (frame == 0) {
			cd.setPx((int)(GameCourt.COURT_WIDTH/2.0 - cd.getWidth()/2.0));
			cd.setPy(620);
			cd.Xdes = cd.getPx();
			cd.Ydes = (int)(GameCourt.COURT_HEIGHT/2.0 - cd.getHeight()/2.0);
			cd.scale = 1.0f;
			cd.scaleDes = 1.0f;
			cd.speed = 0.25f;
		}
		else if (frame == 80) {
			cd.Ydes = 620;
		}
		else if (frame == 160) {
			court.endEventDisplay();
		}
		
		cd.update();
		frame++;
	}
	
	public void draw(Graphics g) {
		cd.draw(g);
	}
}
