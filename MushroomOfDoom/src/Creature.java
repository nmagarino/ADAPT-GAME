import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Creature extends GameObj{
	public int spaceX, spaceY;
	public float animX, animY;
	public Trait[] traits;
	public boolean hasLegs;
	public boolean hasFlagellum;
	protected GameCourt court;
	
	private CreaturePath currentPath;
	private int pathTick;
	private int currTile;
	
	private boolean animating;
	
	public Creature(int px, int py, GameCourt court) {
		super(0, 0, px * (GameCourt.COURT_WIDTH/GameCourt.BOARD_DIMS), py * (GameCourt.COURT_HEIGHT/GameCourt.BOARD_DIMS), 15, 15, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		animX = spaceX = px;
		animY = spaceY = py;
		this.court = court;
		traits = new Trait[3];
		hasLegs = false;
		hasFlagellum = true;
//		traits[0] = court.traitDeck.traitArray[1];
//		traits[1] = court.traitDeck.traitArray[2];
//		traits[2] = court.traitDeck.traitArray[3];
	}
	
	public int getMovement() {
		// default movement is 5, bonus adds on to this
		int sum = 5;
		for (int i = 0; i < traits.length; i++) {
			if (traits[i] != null) {
				sum += traits[i].movementBonus;
			}
		}
		return sum;
	}
	
	public int getCombat() {
		// default combat is 2, bonus adds on to this
		int sum = 2;
		for (int i = 0; i < traits.length; i++) {
			if (traits[i] != null) {
				sum += traits[i].attackBonus;
			}
		}
		return sum;
	}
	
	public void animateAlongPath(CreaturePath path) {
		currentPath = path;
		pathTick = 0;
		currTile = 0;
		animating = true;
		animX = spaceX;
		animY = spaceY;
	}
	
	public void update() {
		int speed = 3;
		if (currentPath != null) {
			
			if (pathTick == speed) {
				pathTick = 0;
				currTile++;
			}
			
			if (currTile == currentPath.getLength()) {
				currentPath = null;
				animating = false;
				this.stopAnimating();
				animX = spaceX;
				animY = spaceY;
			}
			else {
				BoardTile t1 = currentPath.getTiles().get(currTile);
				BoardTile t2 = currentPath.getTiles().get(currTile + 1);
				float u = (float)pathTick/(float)speed;
				animX = (float)(t1.spaceX * (1.0f - u)) + (float)(t2.spaceX * u);
				animY = (float)(t1.spaceY * (1.0f - u)) + (float)(t2.spaceY * u);
				pathTick++;
			}
		}
		setPx((int)(animX * ((float)GameCourt.COURT_WIDTH/(float)GameCourt.BOARD_DIMS)));
		setPy((int)(animY * ((float)GameCourt.COURT_HEIGHT/(float)GameCourt.BOARD_DIMS)));
	}
	
	protected void stopAnimating() {
		court.stopAnimating();
	}

	private Pos minDistance(CreaturePath[][] d, boolean[][] sptSet) { 
		int min = 100;
		int min_indexX = 0;
		int min_indexY = 0;

		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[0].length; j++) {
				if (!sptSet[i][j] && d[i][j].getLength() <= min) {
					min = d[i][j].getLength();
					min_indexX = i;
					min_indexY = j;
				}
			}
		}

		return new Pos(min_indexX, min_indexY); 
	}
	
	public void moveCreatureToTile(int x, int y) {
		court.board.board[spaceX][spaceY].creatureOnTile = null;
		spaceX = x;
		spaceY = y;
		court.board.board[spaceX][spaceY].creatureOnTile = this;
	}
	
	public Map<BoardTile, CreaturePath> getPotentialPaths() {
		int move = getMovement();
		
		boolean[][] sptSet = new boolean[move * 2 + 1][move * 2 + 1];
		CreaturePath[][] d = new CreaturePath[move * 2 + 1][move * 2 + 1];
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[0].length; j++) {
				sptSet[i][j] = false;
				d[i][j] = new CreaturePath(100);
			}
		}
		d[move][move] = new CreaturePath();
		d[move][move].addTile(court.board.board[spaceX][spaceY], 0);
		
		for (int i = -move; i <= move; i++) {
			for (int j = -move; j <= move; j++) {
				Pos u = minDistance(d, sptSet);
				int boardX = spaceX + u.x - move;
				int boardY = spaceY + u.y - move;
				if (boardX < 0 || boardX >= court.board.board.length || boardY < 0 || boardY >= court.board.board[0].length) continue;
				BoardTile tile = court.board.board[boardX][boardY];
				
				sptSet[u.x][u.y] = true;
				//if (d[u.x][u.y].getLength() <= move) tile.type = BoardTile.EnumTileType.GOAL;
				
				for (int a = 0; a < tile.adjacent.length; a++) {
					BoardTile adj = tile.adjacent[a];
					if (adj == null) continue;
					if (Math.abs(adj.spaceX - spaceX) > move || Math.abs(adj.spaceY - spaceY) > move) continue;
					
					Pos v = new Pos(adj.spaceX - spaceX + move, adj.spaceY - spaceY + move);
					int weight = adj.getWeight(this, d[u.x][u.y].getLength());
					if (
							!sptSet[v.x][v.y] &&
							d[u.x][u.y].getLength() + weight < d[v.x][v.y].getLength()
						) {
						d[v.x][v.y] = new CreaturePath(d[u.x][u.y]);
						d[v.x][v.y].addTile(adj, weight);
					}
				}
			}
		}
		
		Map<BoardTile, CreaturePath> validPaths = new HashMap<BoardTile, CreaturePath>();
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[0].length; j++) {
				if (d[i][j].getLength() <= move && !d[i][j].getTiles().isEmpty() && (d[i][j].getEnd().creatureOnTile == null || d[i][j].getEnd().creatureOnTile == this)) validPaths.put(d[i][j].getEnd(), d[i][j]);
			}
		}
		
		return validPaths;
	}
	
	public boolean getLandMovement() {
		return hasLegs;
	}
	
	public boolean getWaterMovement() {
		for (int i = 0; i < traits.length; i++) {
			if (traits[i] != null && traits[i].waterMovement) return true;
		}
		return hasFlagellum;
	}
	
	public boolean getJungleMovement() {
		for (int i = 0; i < traits.length; i++) {
			if (traits[i] != null && traits[i].jungleMovement) return true;
		}
		return false;
	}
	
	public boolean getTundraMovement() {
		for (int i = 0; i < traits.length; i++) {
			if (traits[i] != null && traits[i].tundraMovement) return true;
		}
		return false;
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
			g.fillOval(
					getPx(),
					getPy(),
					getWidth(), getHeight()
					);
	}
}
