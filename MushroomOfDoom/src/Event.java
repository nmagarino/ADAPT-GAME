import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Event {
    String name;
    String description; // Actual text of the trait?
    String warning;
    String filename;
    Image image;
    
    // not all events cause death! xoxo
    boolean causeDeath;
    boolean giveWarning;
    boolean migrate;
    boolean loseTurn;
    
    // traits needed to avoid death
    boolean needsWater;
    boolean needsRainforest;
    boolean needsTundra;
    boolean needsNocturnalism;
    
    // Chance that this trait will appear in the deck
    float probability; 
    
    public Event(String name, String description, String warnMsg, String filename, 
            boolean death, boolean warn, boolean move, boolean lose,
                boolean water, boolean jungle, boolean tundra, boolean sleep,
                float prob) {
        
        this.name = name;
        this.description = description;
        this.warning = warnMsg;
        this.filename = filename;
        
        this.causeDeath = death;
        this.giveWarning = warn;
        this.migrate = move;
        this.loseTurn = lose;
       
        this.needsWater = water;
        this.needsRainforest = jungle;
        this.needsTundra = tundra;
        this.needsNocturnalism = sleep;
         
        this.probability = prob;
        
        if (this.filename != "") {
            try {
                image = ImageIO.read(new File("resources/textures/" + filename));
            }
            catch (IOException e) {
                System.out.println("Internal Error:" + e.getMessage());
            }
        }
    }
    
}
