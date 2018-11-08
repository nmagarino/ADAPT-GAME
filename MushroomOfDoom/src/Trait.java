import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Trait {
	public String name;
	public String description; // Actual text of the trait
	public String imgFileName; // Name of image file for this trait
	public Image img;
	
	// "Dice roll" bonuses
	public int movementBonus = 0;
	public int attackBonus = 0;
	
	// Movement advantages
	public boolean waterMovement;
	public boolean jungleMovement;
	public boolean tundraMovement;
	// Specific Traits
	public boolean nocturnalism;
	
	public float probability; // Chance that this trait will appear in the deck
	
	public Trait(String name, String description, String imgFileName,
				 int movBonus, int attBonus, 
				 boolean wtrMov, boolean jglMov, boolean tdrMov, boolean noct, 
				 float prob) {
		this.name = name;
		this.description = description;
		this.imgFileName = imgFileName;
		this.movementBonus = movBonus;
		this.attackBonus = attBonus;
		this.waterMovement = wtrMov;
		this.jungleMovement = jglMov;
		this.tundraMovement = tdrMov;
		this.nocturnalism = noct;
		this.probability = prob;
		
		if (imgFileName != "") {
	    	try {
	            img = ImageIO.read(new File("resources/textures/" + imgFileName));
	    	}
	    	catch (IOException e) {
	            System.out.println("Internal Error:" + e.getMessage());
	        }
		}
	}
}
