import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Trait extends Card {
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
		super(name, description, imgFileName);
		this.movementBonus = movBonus;
		this.attackBonus = attBonus;
		this.waterMovement = wtrMov;
		this.jungleMovement = jglMov;
		this.tundraMovement = tdrMov;
		this.nocturnalism = noct;
		this.probability = prob;
	}
}
