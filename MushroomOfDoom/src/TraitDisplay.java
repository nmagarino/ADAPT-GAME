import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

public class TraitDisplay extends GameObj {
	public Trait trait;
	public float scale;
	
	public int Xdes, Ydes, Zdes = 0;
	public float scaleDes = 1;
	
	public static int defaultWidth = 180;
	public static int defaultHeight = 300;

	public TraitDisplay(Trait trait, GameCourt court) {
		super(0, 0, 70, 50, defaultWidth, defaultHeight, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		this.trait = trait;
		scale = 1;
	}
	
	public void update() {
		setPx((int)(getPx() + 0.4 * (Xdes - getPx())));
		setPy((int)(getPy() + 0.4 * (Ydes - getPy())));
		scale = (float) (scale + 0.4 * (scaleDes - scale));
		
		this.setHeight((int) (defaultHeight * scale));
		this.setWidth((int) (defaultWidth * scale));
	}
	
	public void draw(Graphics g) {
		if (trait == null || trait.name == "None") {
			g.setColor(new Color(102, 51, 0));
			g.drawRect(this.getPx(), this.getPy(), getWidth(), getHeight());
		}
		else {
			g.setColor(new Color(102, 51, 0));
			g.fillRect((int)(this.getPx() - 3 * scale), (int)(this.getPy() - 3 * scale), (int)(getWidth() + 6 * scale), (int)(getHeight() + 6 * scale));
			g.setColor(new Color(204, 102, 0));
			g.fillRect(this.getPx(), this.getPy(), getWidth(), getHeight());
			
			if (trait.img != null) g.drawImage(trait.img, this.getPx(), this.getPy(), this.getWidth(), (int)(this.getHeight() * 0.6), null);

			Rectangle dispRect = new Rectangle(this.getPx(), this.getPy(), getWidth(), getHeight());
			
			g.setColor(Color.WHITE);
			dispRect.translate(0, (int) (getHeight() * 0.72));
			drawCenteredString(g, trait.name, dispRect, new Font("Helvetica", Font.PLAIN, (int)(30 * scale)));
			
			dispRect.translate(0, (int) (getHeight() * 0.08));
			drawCenteredString(g, trait.description, dispRect, new Font("Helvetica", Font.PLAIN, (int)(20 * scale)));
		}

	}
	
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Set the font
	    g.setFont(font);
	    	    
	    String[] words = text.split(" ");
	    String toWrite = words[0];
	    int lineCount = 0;
	    int lineSpacing = (int)(getHeight() * 0.08);
	    for (int i = 1; i < words.length; i++) {		    
	    	if (metrics.stringWidth(toWrite + " " + words[i]) < rect.width) {
	    		toWrite = toWrite + " " + words[i];
	    	}
	    	else {
	    		// Determine the X coordinate for the text
	    	    int x = rect.x + (rect.width - metrics.stringWidth(toWrite)) / 2;
	    	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    	    int y = rect.y + lineSpacing * lineCount;
	    	    // Draw the String
	    	    g.drawString(toWrite, x, y);
	    	    
	    	    toWrite = words[i];
	    	    lineCount++;
	    	}
	    }
	    int x = rect.x + (rect.width - metrics.stringWidth(toWrite)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + lineSpacing * lineCount;
	    // Draw the String
	    g.drawString(toWrite, x, y);
	    
	}
	
}
