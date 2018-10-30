
public class Player extends Creature {
	private int homeX;
	private int homeY;
	
	public Player(int posX, int posY, GameCourt court) {
		super(posX, posY, court);
		this.homeX = posX;
		this.homeY = posY;
	}
}
