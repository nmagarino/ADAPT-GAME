
public class Player extends Creature {
	private int homeX;
	private int homeY;
	private boolean shouldEvolve;
	
	public Player(int posX, int posY, GameCourt court) {
		super(posX, posY, court);
		this.homeX = posX;
		this.homeY = posY;
		shouldEvolve = false;
	}
	
	@Override
	public void moveCreatureToTile(int x, int y) {
		super.moveCreatureToTile(x, y);
		if (court.board.board[x][y].type == BoardTile.EnumTileType.NEST) shouldEvolve = true;
	}
	
	@Override
	protected void stopAnimating() {
		super.stopAnimating();
		if (shouldEvolve) evolve();
	}
	
	public void evolve() {
		shouldEvolve = false;
		court.board.board[spaceX][spaceY].useNest();
		Trait newTrait = court.traitDeck.getRandomTrait();
		int whichTrait = 0;
		boolean userPick = false;
		if (traits[0] == null || traits[0].name == "None") {
			whichTrait = 0;
		}
		else if (traits[1] == null || traits[1].name == "None") {
			whichTrait = 1;
		}
		else if (traits[2] == null || traits[2].name == "None") {
			whichTrait = 2;
		}
		else {
			userPick = true;
		}
		court.displayEvolving(this, newTrait, whichTrait, userPick);
	}
}
