import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Event extends Card {
    String warning;
    
    // not all events cause death! xoxo
//    boolean causeDeath;
//    boolean giveWarning;
//    boolean migrate;
//    boolean loseTurn;
    
    // traits needed to avoid death
//    boolean needsWater;
//    boolean needsRainforest;
//    boolean needsTundra;
//    boolean needsNocturnalism;
    
    // Chance that this trait will appear in the deck
    float probability; 
    
    public Event(String name, String description, String warnMsg, String filename, 
            boolean death, boolean warn, boolean move, boolean lose,
                boolean water, boolean jungle, boolean tundra, boolean sleep,
                float prob) {
    	super(name, description, filename);
        this.warning = warnMsg;
//        this.giveWarning = warn;

//        this.causeDeath = death;
//        this.migrate = move;
//        this.loseTurn = lose;
//       
//        this.needsWater = water;
//        this.needsRainforest = jungle;
//        this.needsTundra = tundra;
//        this.needsNocturnalism = sleep;
         
        this.probability = prob;
    }
    
    public Event(String name, String description, String warnMsg, String filename) {
    	super(name, description, filename);
    	this.warning = warnMsg;
    }
    
    public void doEffect() {
    	
    }
    
}
