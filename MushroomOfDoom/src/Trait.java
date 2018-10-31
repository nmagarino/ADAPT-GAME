
public class Trait {
	String name;
	String description; // Actual text of the trait?
	int movementBonus = 0;
	int attackBonus = 0;
	boolean waterMovement;
	boolean jungleMovement;
	boolean tundraMovement;
	boolean nocturnalism;
	float probability; // Chance that this trait will appear in the deck
	
	public Trait(String name, int movBonus, int attBonus, boolean wtrMov, 
				boolean jglMov, boolean tdrMov, boolean noct, float prob) {
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
