import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Creature extends GameObj {
	public int id;
	public static int numCreatures = 0;
	
	public int spaceX, spaceY;
	public float animX, animY;
	public Trait[] traits;
	public Leg legs;
	public Flag flagellum;
	protected GameCourt court;
	
	private CreaturePath currentPath;
	private int pathTick;
	private int currTile;
	private int moveTime;
	
	private Creature shouldFight = null;
	
	public Color color;
	
	private boolean animating;
	
	public boolean loseTurn = false;
	
	public Creature(int px, int py, GameCourt court, Color color) {
		super(0, 0, px * (GameCourt.COURT_WIDTH/GameCourt.BOARD_DIMS), py * (GameCourt.COURT_HEIGHT/GameCourt.BOARD_DIMS), 15, 15, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		id = numCreatures;
		numCreatures++;
		
		animX = spaceX = px;
		animY = spaceY = py;
		this.court = court;
		court.board.board[spaceX][spaceY].creatureOnTile = this;
		traits = new Trait[3];
		legs = null;
		flagellum = new Flag();
//		traits[0] = court.traitDeck.traitArray[1];
//		traits[1] = court.traitDeck.traitArray[2];
//		traits[2] = court.traitDeck.traitArray[3];
		this.color = color;
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
		moveTime = 0;
		currTile = 0;
		animating = true;
		animX = spaceX;
		animY = spaceY;
	}
	
	public void update() {
		int speed = 3;
		if (currentPath != null) {
			moveTime++;
			if (moveTime > 100) {
				currentPath = null;
				animating = false;
				this.stopAnimating();
				animX = spaceX;
				animY = spaceY;
			}
			else {
			
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
					if (currTile+1 == currentPath.getTiles().size()) {
						animX = currentPath.getEnd().spaceX;
						animY = currentPath.getEnd().spaceY;
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
			}
		}
		else {
			animX = spaceX;
			animY = spaceY;
		}
		setPx((int)(animX * ((float)GameCourt.COURT_WIDTH/(float)GameCourt.BOARD_DIMS)));
		setPy((int)(animY * ((float)GameCourt.COURT_HEIGHT/(float)GameCourt.BOARD_DIMS)));
	}
	
	protected void stopAnimating() {
		court.stopAnimating();
		if (shouldFight != null) {
			court.fight(this, shouldFight, court.board.board[spaceX][spaceY]);
			shouldFight = null;
		}
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
		Creature onTile = court.board.board[spaceX][spaceY].creatureOnTile;
		if (onTile != null) {
			shouldFight = onTile;
		}
		else {
			court.board.board[spaceX][spaceY].creatureOnTile = this;
		}
	}
	
	public void die(Creature killer) {
		if (killer instanceof Player) {
			court.userPickStealTrait = true;
		}
		else {
			int howManyTraits = 0;
			for (int i = 0; i < traits.length; i++) {
				if (traits[i] != null && traits[i].name != "None") howManyTraits++;
			}
			int whichTrait = (int)Math.floor(Math.random() * (double)howManyTraits);
			int traitCounter = -1;
			for (int i = 0; i < traits.length; i++) {
				if (traits[i] != null && traits[i].name != "None") traitCounter++;
				if (traitCounter == whichTrait) {
					traits[i] = null;
					break;
				}
			}
		}
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
				if (d[i][j].getLength() <= move && !d[i][j].getTiles().isEmpty() && d[i][j].getEnd().whosHome == null) validPaths.put(d[i][j].getEnd(), d[i][j]);
			}
		}
		
		return validPaths;
	}
	
	public boolean getLandMovement() {
		return legs != null;
	}
	
	public boolean getWaterMovement() {
		for (int i = 0; i < traits.length; i++) {
			if (traits[i] != null && traits[i].waterMovement) return true;
		}
		return flagellum != null;
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
		g.setColor(color);
		g.fillOval(
			getPx() + 2,
			getPy() + 2,
			getWidth() - 4, getHeight() - 4
		);
	}
}
