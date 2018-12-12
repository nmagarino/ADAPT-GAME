import java.awt.Color;
import java.awt.Graphics;

public class BoardTile extends GameObj {
	public enum EnumTileType {
		WATER,
		LAND,
		FOREST,
		TUNDRA,
		ROCK,
		NEST,
		NEST_INACTIVE,
		GOAL,
		START,
		BEACH,
		BEACH_FLOODED
	}
	
	EnumTileType type;
	
	int spaceX, spaceY;
	
	public BoardTile up;
	public BoardTile down;
	public BoardTile left;
	public BoardTile right;
	
	public int turnsUntilNestActive;
	public int turnsUntilBeach;
	
	public BoardTile[] adjacent;
	
	public Creature creatureOnTile;
	public Creature whosHome;
	
	public BoardTile(EnumTileType type, int posX, int posY) {
		super(0, 0,
				posX * (GameCourt.COURT_WIDTH/GameCourt.BOARD_DIMS),
				posY * (GameCourt.COURT_HEIGHT/GameCourt.BOARD_DIMS),
				(GameCourt.COURT_WIDTH/GameCourt.BOARD_DIMS),
				(GameCourt.COURT_HEIGHT/GameCourt.BOARD_DIMS),
				GameCourt.COURT_WIDTH,
				GameCourt.COURT_HEIGHT);
		this.type = type;
		spaceX = posX;
		spaceY = posY;
		
		adjacent = new BoardTile[4];
		
		creatureOnTile = null;
	}
	
	public void useNest() {
		if (this.type == EnumTileType.NEST) {
			this.turnsUntilNestActive = 3;
			this.type = EnumTileType.NEST_INACTIVE;
		}
	}
	
	public boolean isNestOnLand() {
		if (up != null && (up.type == EnumTileType.LAND || up.type == EnumTileType.FOREST || up.type == EnumTileType.TUNDRA || up.type == EnumTileType.BEACH)) return true;
		if (down != null && (down.type == EnumTileType.LAND || down.type == EnumTileType.FOREST || down.type == EnumTileType.TUNDRA || down.type == EnumTileType.BEACH)) return true;
		if (right != null && (right.type == EnumTileType.LAND || right.type == EnumTileType.FOREST || right.type == EnumTileType.TUNDRA || right.type == EnumTileType.BEACH)) return true;
		if (left != null && (left.type == EnumTileType.LAND || left.type == EnumTileType.FOREST || left.type == EnumTileType.TUNDRA || left.type == EnumTileType.BEACH)) return true;
		return false;
	}
	
	public int getWeight(Creature creature, int currDist) {
		if (type == EnumTileType.ROCK) {
			return 100;
		}
		else if (type == EnumTileType.LAND || type == EnumTileType.FOREST || type == EnumTileType.TUNDRA || type == EnumTileType.BEACH) {
			if (creature.getLandMovement()) {
				if (type == EnumTileType.FOREST && creature.getJungleMovement()) {
					return 1;
				}
				else if (type == EnumTileType.TUNDRA && creature.getTundraMovement()) {
					return 1;
				}
				else if (type == EnumTileType.BEACH || type == EnumTileType.LAND) {
					return 1;
				}
				return Math.max(creature.getMovement() - currDist, 1);
			}
			else return 100;
		}
		else if (type == EnumTileType.WATER || type == EnumTileType.BEACH_FLOODED) {
			if (creature.getWaterMovement()) {
				return 1;
			}
			return Math.max(creature.getMovement() - currDist, 1);
		}
		return 1;
	}
	
	@Override
    public void draw(Graphics g) {
		if (type == EnumTileType.WATER) {
			g.setColor(new Color(45, 110, 190));
		}
		else if (type == EnumTileType.LAND) {
			g.setColor(new Color(55, 190, 45));
		}
		else if (type == EnumTileType.FOREST) {
			g.setColor(new Color(23, 97, 17));
		}
		else if (type == EnumTileType.TUNDRA) {
			g.setColor(new Color(255, 255, 255));
		}
		else if (type == EnumTileType.ROCK) {
			g.setColor(new Color(138, 138, 138));
		}
		else if (type == EnumTileType.NEST) {
			g.setColor(new Color(187, 46, 47));
		}
		else if (type == EnumTileType.NEST_INACTIVE) {
			g.setColor(new Color(140, 26, 23));
		}
		else if (type == EnumTileType.GOAL) {
			g.setColor(new Color(255, 246, 99));
		}
		else if (type == EnumTileType.START) {
			g.setColor(new Color(72, 255, 240));
		}
		else if (type == EnumTileType.BEACH) {
			g.setColor(new Color(201, 200, 152));
		}
		else if (type == EnumTileType.BEACH_FLOODED) {
			g.setColor(new Color(45, 110, 190));
		}
		else {
			g.setColor(new Color(45, 110, 190));
		}
		
		if (whosHome != null) {
			g.setColor(Color.BLACK);
			g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
			g.setColor(whosHome.color);
			g.fillRect(this.getPx() + 2, this.getPy() + 2, this.getWidth() - 4, this.getHeight() - 4);
		}
		else {
			g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
		}
    }
}
