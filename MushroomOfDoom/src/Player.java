
public class Player extends Creature {
	private int homeX;
	private int homeY;
	
	public Player(int posX, int posY) {
		super(posX, posY);
		this.homeX = posX;
		this.homeY = posY;
	}
}
