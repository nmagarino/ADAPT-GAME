import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

public class CardDisplay extends GameObj {
	public Card card;
	public float scale;
	
	public int Xdes, Ydes, Zdes = 0;
	public float scaleDes = 1;
	public float speed = 0.4f;
	
	public static int defaultWidth = 180;
	public static int defaultHeight = 270;
	
	public Color color;

	public CardDisplay(Card card, GameCourt court) {
		super(0, 0, 70, 50, defaultWidth, defaultHeight, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		this.card = card;
		scale = 1;
		
		if (card instanceof Trait) {
			color = new Color(204, 102, 0);
		}
		else if (card instanceof Event) {
			color = new Color(153, 204, 255);
		}
		else if (card instanceof Leg) {
			color = Color.CYAN;
		}
		else if (card instanceof Flag) {
			color = Color.GREEN;
		}
		else {
			color = Color.BLACK;
		}
	}
	
	public void update() {
		setPx((int)(getPx() + speed * (Xdes - getPx())));
		setPy((int)(getPy() + speed * (Ydes - getPy())));
		scale = (float) (scale + speed * (scaleDes - scale));
		
		this.setHeight((int) (defaultHeight * scale));
		this.setWidth((int) (defaultWidth * scale));
	}
	
	public void draw(Graphics g) {
		if (card == null || card.name == "None") {
			g.setColor(new Color((int)(color.getRed() * 0.5f), (int)(color.getGreen() * 0.5f), (int)(color.getBlue() * 0.5f), 100));
			g.fillRect(this.getPx(), this.getPy(), getWidth(), getHeight());
		}
		else {
			g.setColor(new Color((int)(color.getRed() * 0.5f), (int)(color.getGreen() * 0.5f), (int)(color.getBlue() * 0.5f)));
			g.fillRect((int)(this.getPx() - 3 * scale), (int)(this.getPy() - 3 * scale), (int)(getWidth() + 6 * scale), (int)(getHeight() + 6 * scale));
			g.setColor(color);
			g.fillRect(this.getPx(), this.getPy(), getWidth(), getHeight());
			
			if (card.img != null) g.drawImage(card.img, this.getPx(), this.getPy(), this.getWidth(), this.getWidth(), null);

			Rectangle dispRect = new Rectangle(this.getPx(), this.getPy(), getWidth(), getHeight());
			
			g.setColor(Color.WHITE);
			dispRect.translate(0, (int) (getHeight() * 0.78));
			drawCenteredString(g, card.name, dispRect, new Font("Helvetica", Font.PLAIN, (int)(30 * scale)));
			
			dispRect.translate(0, (int) (getHeight() * 0.09));
			drawCenteredString(g, card.description, dispRect, new Font("Helvetica", Font.PLAIN, (int)(20 * scale)));
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
