import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class GameBoard {
    public BoardTile[][] board;
    public List<BoardTile> startTiles;
    
    public GameBoard(int boardX, int boardY) {
    	board = new BoardTile[boardX][boardY];
    	
    	for (int i = 0; i < boardX; i++) {
    		for (int j = 0; j < boardY; j++) {
    			board[i][j] = new BoardTile(BoardTile.EnumTileType.WATER, i, j);
    		}
    	}
    	
    	startTiles = new ArrayList<BoardTile>();
    }
    
    public void readMap() {
        BufferedImage img;

    	try {
            img = ImageIO.read(new File("resources/textures/AdaptMapt.png"));
            board = new BoardTile[img.getWidth()][img.getHeight()];
            
            for (int i = 0; i < board.length; i++) {
            	for (int j = 0; j < board[0].length; j++) {
            		int rgb = img.getRGB(i, j);
//            		System.out.println(rgb + ",    " + i + ", " + j);	//Uncomment this to print color values if you want to add a new tile type
            		if (rgb == -13799746) {
            			board[i][j] = new BoardTile(BoardTile.EnumTileType.WATER, i, j);
            		}
            		else if (rgb == -4313811) {
            			board[i][j] = new BoardTile(BoardTile.EnumTileType.NEST, i, j);
            		}
            		else if (rgb == -13124051) {
            			board[i][j] = new BoardTile(BoardTile.EnumTileType.LAND, i, j);
            		}
            		else if (rgb == -11993104) {
            			board[i][j] = new BoardTile(BoardTile.EnumTileType.START, i, j);
            			startTiles.add(board[i][j]);
            		}
            		else if (rgb == -15245039) {
            			board[i][j] = new BoardTile(BoardTile.EnumTileType.FOREST, i, j);
            		}
            		else if (rgb == -7697782) {
            			board[i][j] = new BoardTile(BoardTile.EnumTileType.ROCK, i, j);
            		}
            		else if (rgb == -1) {
            			board[i][j] = new BoardTile(BoardTile.EnumTileType.TUNDRA, i, j);
            		}
            		else if (rgb == -2461) {
            			board[i][j] = new BoardTile(BoardTile.EnumTileType.GOAL, i, j);
            		}
            		else {
            			board[i][j] = new BoardTile(BoardTile.EnumTileType.BEACH, i, j);
            		}
                }
            }
            
            for (int i = 0; i < board.length; i++) {
            	for (int j = 0; j < board[0].length; j++) {
            		if (j == 0) {
            			board[i][j].up = null;
            		}
            		else {
            			board[i][j].up = board[i][j - 1];
            		}
            		
            		if (j == board.length - 1) {
            			board[i][j].down = null;
            		}
            		else {
            			board[i][j].down = board[i][j + 1];
            		}
            		
            		if (i == 0) {
            			board[i][j].left = null;
            		}
            		else {
            			board[i][j].left = board[i - 1][j];
            		}
            		
            		if (i == board[0].length - 1) {
            			board[i][j].right = null;
            		}
            		else {
            			board[i][j].right = board[i + 1][j];
            		}
            	}
            }
            
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    public void draw(Graphics g) {
    	for (int i = 0; i < board.length; i++) {
        	for (int j = 0; j < board[0].length; j++) {
            	board[i][j].draw(g);
            }
        }
    }
}
