
public class TraitDeck {
	Trait[] traitArray;
	
	public TraitDeck() {
		traitArray = new Trait[] {
				new Trait("None", 0, 0, false, false, false, false, .0f),
				new Trait("Fangs", 0, 1, false, false, false, false, .1f),
				new Trait("Fur Coat", 0, 0,  false, false, true, false, .1f),
				new Trait("Fins", 0, 0, true, false, false, false, .1f),
				new Trait("Sail", 1, 0, false, false, false, false, .1f),
				new Trait("Hooves", 1, 0, false, false, false, false, .1f)
		};
	}
	
	public Trait getRandomTrait() {
		return new Trait("None", 0, 0, false, false, false, false, .0f);
	}
}
