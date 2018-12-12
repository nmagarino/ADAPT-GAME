import java.awt.Color;

public class Player extends Creature {
	public int homeX;
	public int homeY;
	private boolean shouldEvolve;
	private boolean shouldWin = false;
	public int number;
	
	public Player(int posX, int posY, GameCourt court, Color color, int number) {
		super(posX, posY, court, color);
		this.homeX = posX;
		this.homeY = posY;
		court.board.board[homeX][homeY].whosHome = this;
		shouldEvolve = false;
		this.number = number;
	}
	
	@Override
	public void moveCreatureToTile(int x, int y) {
		super.moveCreatureToTile(x, y);
		if (court.board.board[x][y].type == BoardTile.EnumTileType.NEST) shouldEvolve = true;
		if (court.board.board[x][y].type == BoardTile.EnumTileType.GOAL) {
			shouldWin = true;
		}
	}
	
	@Override
	public void die(Creature killer) {
		super.die(killer);
		spaceX = homeX;
		spaceY = homeY;
		Creature onTile = court.board.board[spaceX][spaceY].creatureOnTile;
	}
	
	@Override
	protected void stopAnimating() {
		super.stopAnimating();
		if (shouldEvolve) evolve(court.traitDeck.getRandomTrait());
		if (shouldWin){
			court.victoryDisplay = new VictoryDisplay(this, court);
			court.playing = false;
		}
	}
	
	public void evolve(Trait newTrait) {
		shouldEvolve = false;
		court.board.board[spaceX][spaceY].useNest();
		court.board.board[homeX][homeY].whosHome = null;
		homeX = spaceX;
		homeY = spaceY;
		court.board.board[homeX][homeY].whosHome = this;
		if (court.board.board[homeX][homeY].isNestOnLand() && hasLegs) {
			hasFlagellum = false;
		}
		
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
		boolean hasAllTraits = true;
		for (int i = 0; i < traits.length; i++) {
			if (traits[0] == null || traits[0].name == "None") hasAllTraits = false;
		}
		if (hasAllTraits) {
			hasLegs = true;
		}
	}
}
