
public class Trait {
	String name;
	String description; // Actual text of the trait
	String imgFileName; // Name of image file for this trait
	
	// "Dice roll" bonuses
	int movementBonus = 0;
	int attackBonus = 0;
	
	// Movement advantages
	boolean waterMovement;
	boolean jungleMovement;
	boolean tundraMovement;
	// Specific Traits
	boolean nocturnalism;
	
	float probability; // Chance that this trait will appear in the deck
	
	public Trait(String name, String description, String imgFileName,
				 int movBonus, int attBonus, 
				 boolean wtrMov, boolean jglMov, boolean tdrMov, boolean noct, 
				 float prob) {
		this.name = name;
		this.movementBonus = movBonus;
		this.attackBonus = attBonus;
		this.waterMovement = wtrMov;
		this.jungleMovement = jglMov;
		this.tundraMovement = tdrMov;
		this.nocturnalism = noct;
		this.probability = prob;
	}
}
