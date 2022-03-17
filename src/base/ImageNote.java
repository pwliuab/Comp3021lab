package base;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
public class ImageNote extends Note implements java.io.Serializable {
	File image;
	public ImageNote(String title) {
		super(title);
	}
}
