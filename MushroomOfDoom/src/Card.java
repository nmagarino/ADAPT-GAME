import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Card {
	public String name;
	public String description; // Actual text of the trait
	public String imgFileName; // Name of image file for this trait
	public Image img;
	
	public Card(String name, String description, String imgFileName) {
		this.name = name;
		this.description = description;
		this.imgFileName = imgFileName;
		
		if (imgFileName != "") {
	    	try {
	            img = ImageIO.read(new File("resources/textures/" + imgFileName));
	    	}
	    	catch (IOException e) {
	            System.out.println("Internal Error:" + e.getMessage());
	        }
		}
	}
}
