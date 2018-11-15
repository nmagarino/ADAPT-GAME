
public class TraitDeck {
	Trait[] traitArray;
	
	public TraitDeck() {
		// Contains all traits in the game
		traitArray = new Trait[] {
				new Trait(
						"None", "", "",
						0, 0, 
						false, false, false, false, 
						.0f),
				new Trait(
						"Fangs", "Adds +1 to attack.", "Trait-Fangs.JPG",
						0, 1, 
						false, false, false, false, 
						.1f),
				new Trait(
						"Fur Coat", "You can move normally through Tundra.", "Trait-FurCoat.JPG",
						0, 0,  
						false, false, true, false, 
						.3f),
				new Trait(
						"Fins", "You can move normally through Water.", "Trait-Fins.JPG", 
						0, 0, 
						true, false, false, false, 
						.3f),
				new Trait(
						"Sail", "Adds +1 to movement.", "Trait-Sail.JPG", 
						1, 0, 
						false, false, false, false, 
						.5f),
				new Trait(
						"Hooves", "Adds +1 to movement.", "Trait-Hooves.JPG", 
						1, 0, 
						false, false, false, false, 
						.5f),
				new Trait(
						"Gills", "You can move normally through Water.", "Trait-Gills.JPG", 
						0, 0, 
						true, false, false, false, 
						.3f),
				new Trait(
						"Setae", "You can move normally through Rainforest.", "Trait-Setae.JPG", 
						0, 0, 
						false, true, false, false, 
						.3f),
				new Trait(
						"Stinger", "Adds +1 to attack.", "Trait-Stinger.JPG", 
						0, 1, 
						false, false, false, false,
						.5f),
				new Trait(
						"Feathers", "Adds +2 to movement.", "Trait-Feathers.JPG", 
						2, 0, 
						false, false, false, false, 
						.4f),
				new Trait(
						"Nocturnalism", "You are able to move during Night.", "Trait-Nocturnalism.JPG", 
						0, 0, 
						false, false, false, true, 
						.3f),
				new Trait(
						"Tail Club", "Adds +2 to attack.", "Trait-TailClub.JPG", 
						0, 2, 
						false, false, false, false, 
						.4f),
				new Trait(
						"Prehensile Tail", "You can move normally through Rainforest.", "Trait-PrehensileTail.JPG", 
						0, 0, 
						false, true, false, false, 
						.3f),
				new Trait(
						"Extra Legs", "Adds +2 to movement.", "Trait-ExtraLegs.JPG", 
						2, 0, 
						false, false, false, false, 
						.4f),
				new Trait(
						"Claws", "Adds +2 to attack.", "Trait-Claws.JPG", 
						0, 2, 
						false, false, false, false, 
						.4f),
				new Trait(
						"Blubber", "You can move normally through Tundra.", "Trait-Blubber.JPG", 
						0, 0, 
						false, false, true, false, 
						.3f),
		};
	}
	
	public Trait getRandomTrait() {
		float sumProbabilities = 0.f;
		for(int i = 0; i < traitArray.length; i++) {
			sumProbabilities += traitArray[i].probability;
		}
		
		double rand = Math.random() * sumProbabilities;
		
		sumProbabilities = 0.f;
		for(int i = 0; i < traitArray.length; i++) {
			sumProbabilities += traitArray[i].probability;
			if(rand < sumProbabilities) {
				return traitArray[i];
			}
		}
		
		return traitArray[0];	
	}
}
