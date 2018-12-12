import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class VictoryDisplay {
	private Player whichPlayer;
	private GameCourt court;

	public VictoryDisplay(Player whichPlayer, GameCourt court) {
		this.whichPlayer = whichPlayer;
		this.court = court;
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, court.COURT_WIDTH, court.COURT_HEIGHT);
		g.setColor(new Color(0, 0, 0, 255));
				
		g.setColor(whichPlayer.color);
		Font f = new Font("Helvetica", Font.PLAIN, 60);
		FontMetrics metrics = g.getFontMetrics(f);
		g.setFont(f);
		String s = "Player " + whichPlayer.number + " wins!";
		
		g.drawString(s, court.COURT_WIDTH/2 - metrics.stringWidth(s)/2, 300);
	}
	
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Set the font
	    g.setFont(font);
	    	    
	    String[] words = text.split(" ");
	    String toWrite = words[0];
	    int lineCount = 0;
	    int lineSpacing = (int)(20);
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
