import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Enemy extends Creature {

	public Enemy(int posX, int posY, GameCourt court) {
		// this position should be a random spot on the map, that is land.
		super(posX, posY, court, Color.WHITE);
		// Enemy starts on land
		this.hasLegs = true;
		this.hasFlagellum = false;

		// Determines how many traits the new creature will have
		// Naturally, more traits means a stronger creature
		int rand = (int) ((Math.random() * 3.0) + 1.0);
		for (int i = 0; i < rand - 1; i++) {
			this.traits[i] = court.traitDeck.getRandomTrait();
		}
		
		//this.traits = new Trait[3]; // check weak creature
	}

	// Decides to move randomly (do nothing), pursue player, or run from player.
	public void doTurn() {
		Player closestPlayer = findClosestPlayer();

		Map<BoardTile, CreaturePath> possPaths = this.getPotentialPaths();
		// pick path from here!
		Set<BoardTile> keys = possPaths.keySet();
		Object[] keysArr = keys.toArray();

		BoardTile toMoveTo = (BoardTile) keysArr[0];

		// If no players in range, move randomly. Pick random spot in range.
		if (closestPlayer == null) {
			System.out.println("Move randomly");

			float sumProbabilities = 0.f;
			for (int i = 0; i < keys.size(); i++) {
				sumProbabilities += 1.0;
			}

			double rand = Math.random() * sumProbabilities;

			sumProbabilities = 0.f;
			for (int i = 0; i < keysArr.length; i++) {
				sumProbabilities += 1.0;
				if (rand < sumProbabilities) {
					toMoveTo = (BoardTile) keysArr[i];
				}
			}

			// this.court.whosTurn++;
		}
		// Pursue closest player. Move to tile closest to player (or to player)
		else if (this.getCombat() >= closestPlayer.getCombat()) {
			System.out.println("Pursue player");

			// Find tile closest to player
			BoardTile closestTile = toMoveTo;
			double minDist = Double.MAX_VALUE;
			for (int i = 0; i < keysArr.length; i++) {
				BoardTile currtile = (BoardTile) keysArr[i];
				double dist = Math.sqrt(Math.pow(currtile.spaceX - closestPlayer.spaceX, 2.0)
						+ Math.pow(currtile.spaceX - closestPlayer.spaceX, 2.0));
				if (dist < minDist) {
					minDist = dist;
					closestTile = currtile;
				}
			}
			System.out.println("Closest player is at " + closestPlayer.spaceX + ", " + closestPlayer.spaceY);
			toMoveTo = closestTile;
			
			
			// or move onto player?
			for (int i = 0; i < keysArr.length; ++i) {
				BoardTile currtile = (BoardTile) keysArr[i];
				if (currtile.spaceX == closestPlayer.spaceX && currtile.spaceY == closestPlayer.spaceY) {
					toMoveTo = currtile;
				}
			}
			
			System.out.println("Moving to space at " + toMoveTo.spaceX + ", " + toMoveTo.spaceY);

		}
		// Flee closest player. Move to tile furthest from player.
		else {
			System.out.println("Flee player");

			// find furthest boardtile from player
			BoardTile furthestTile = toMoveTo;
			double maxDist = Double.MIN_VALUE;
			for (int i = 0; i < keysArr.length; i++) {
				BoardTile currtile = (BoardTile) keysArr[i];
				double dist = Math.sqrt(Math.pow(currtile.spaceX - closestPlayer.spaceX, 2.0)
						+ Math.pow(currtile.spaceX - closestPlayer.spaceX, 2.0));
				if (dist < maxDist) {
					maxDist = dist;
					furthestTile = currtile;
				}
			}

			toMoveTo = furthestTile;

		}

		CreaturePath selectedPath = possPaths.get(toMoveTo);

		// then move enemy
		this.animateAlongPath(selectedPath);
		this.court.isAnimating = true;
		BoardTile last = selectedPath.getTiles().get(selectedPath.getTiles().size() - 1);

		//System.out.println("Moving to space at " + last.spaceX + ", " + last.spaceY);

		this.moveCreatureToTile(last.spaceX, last.spaceY);
	}

	private Player findClosestPlayer() {
		// Enemy only notices a player within a certain range,
		// and acts based on this closest player's power.
		// This A.I. is kind of dumb.

		// Store all players in range
		int range = 5;
		ArrayList<Player> playersInRange = new ArrayList<Player>();
		for (int i = this.spaceX - range; i < this.spaceX + range; i++) {
			for (int j = this.spaceY - range; j < this.spaceY + range; j++) {
				
				// reduce range if range goes outside actual board
				int spaceNumX = i;
				int spaceNumY = j;
				
				if (i < 0) {
					spaceNumX = 0;
				}
				if (i > 39) {
					spaceNumX = 39;
				}
				if (j < 0) {
					spaceNumY = 0;
				}
				if (j > 39) {
					spaceNumY = 39;
				}
				
				//System.out.println("Scanning tile " + spaceNumX + ", " + spaceNumY);
				
				BoardTile currTile = this.court.board.board[spaceNumX][spaceNumY];
				Creature creatureOnTile = currTile.creatureOnTile;
				if (creatureOnTile != null && creatureOnTile.getClass() == Player.class) {
					playersInRange.add((Player) creatureOnTile);
				}
			}
		}

		if (playersInRange.isEmpty()) {
			return null;
		}

		// find minimum distance from these, and return this player
		Player closestPlayer = playersInRange.get(0);
		double leastDist = Double.MAX_VALUE;
		for (int i = 0; i < playersInRange.size(); i++) {
			Player currPlayer = playersInRange.get(i);
			double dist = Math.sqrt(
					Math.pow(this.spaceX - currPlayer.spaceX, 2.0) + Math.pow(this.spaceX - currPlayer.spaceX, 2.0));
			if (dist < leastDist) {
				leastDist = dist;
				closestPlayer = currPlayer;
			}
		}

		return closestPlayer;
	}

	// Color enemies red
	@Override
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
