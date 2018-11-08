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
	
	public BoardTile[] adjacent;
	
	public Creature creatureOnTile;
	
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
		else if (type == EnumTileType.GOAL) {
			g.setColor(new Color(255, 246, 99));
		}
		else if (type == EnumTileType.START) {
			g.setColor(new Color(72, 255, 240));
		}
		else if (type == EnumTileType.BEACH) {
			g.setColor(new Color(201, 200, 152));
		}
		else {
			g.setColor(new Color(45, 110, 190));
		}
        
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}
