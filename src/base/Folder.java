package base;
import java.util.ArrayList;

public class Folder {
	private ArrayList<Note> notes;
	private String name;
	
	public Folder(String name) {
		this.name = new String(name);
		this.notes = new ArrayList<Note>();
	}
	
	public void addNote(Note note) {
		this.notes.add(note);
	}
	
	public String getName() {
		return new String(this.name);
	}
	
	public ArrayList<Note> getNotes() {
		return new ArrayList<Note>(notes);
	}
	
	
	public String toString() {
		int nText = 0;
		int nImage = 0;
		
		for (Note note : this.notes) {
			if (note instanceof TextNote) nText ++;
			if (note instanceof ImageNote) nImage ++;
		}
		
		return this.name + ":" + nText + ":" + nImage;
		
	}
	
	public boolean equals(Object folder) {
		Folder otherFolder = (Folder) folder;
		return otherFolder.getName().equals(this.name);
	}
	
}
