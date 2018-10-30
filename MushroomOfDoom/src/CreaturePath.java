import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CreaturePath {
	private int length;
	private List<BoardTile> tiles;
	
	public boolean mouseOver;
	
	public CreaturePath() {
		tiles = new ArrayList<BoardTile>();
		length = 0;
		mouseOver = false;
	}
	
	public CreaturePath(int length) {
		tiles = new ArrayList<BoardTile>();
		this.length = length;
		mouseOver = false;
	}
	
	public CreaturePath(CreaturePath toCopy) {
		tiles = new ArrayList<BoardTile>();
		for (int i = 0; i < toCopy.tiles.size(); i++) {
			tiles.add(toCopy.tiles.get(i));
		}
		this.length = toCopy.length;
		mouseOver = false;
	}
	
	public void addTile(BoardTile tile, int weight) {
		tiles.add(tile);
		length += weight;
	}
	
	public int getLength() {
		return length;
	}
	
	public BoardTile getStart() {
		return tiles.get(0);
	}
	
	public List<BoardTile> getTiles() {
		return tiles;
	}
	
	public BoardTile getEnd() {
		return tiles.get(tiles.size() - 1);
	}
	
	public void draw(Graphics g, int frame) {
		if (tiles != null && tiles.size() > 0) {
			if (!mouseOver) {
				BoardTile end = getEnd();
				g.setColor(new Color(0, 0, 0, 70 + (int)(30 * Math.sin(frame * 0.2))));
				g.fillRect(end.getPx(), end.getPy(), end.getWidth(), end.getHeight());
			}
			else {
				BoardTile end = getEnd();
				g.setColor(new Color(255, 255, 255, 70));
				g.fillRect(end.getPx(), end.getPy(), end.getWidth(), end.getHeight());
			}
		}
	}
}
