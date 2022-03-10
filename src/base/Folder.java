package base;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Folder implements Comparable<Folder> {
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
	
	public boolean hasKeywords(String keywords, String content) {
	    Stack<Boolean> resultStack = new Stack<Boolean>();
	    // there was an "or" operator in the previous element
	    boolean wasOr = false;
	    boolean previousItem = true;
	    for (String item : keywords.split(" ")) {
			if (item.equals("or")) {
	        	wasOr = true;
	            previousItem = resultStack.pop();
	            continue;
	        }
	        boolean condition = content.contains(item);
	        if (wasOr) condition = content.contains(item) || previousItem;
	 		wasOr =false; 
	        resultStack.push(condition);
	    }
	    boolean result = true;
	    for (boolean condition : resultStack) {
	    	result = result && condition;
	    }

	    return result;
		
	}
	
	public List<Note> searchNotes(String keywords) {
		String lowerCaseKeywords = keywords.toLowerCase();
		List<Note> resultNotes = new ArrayList<Note>();
		for (Note n : this.notes) {
			if (n instanceof TextNote) {
				String content = ((TextNote) n).getContent().toLowerCase();
				String title = ((TextNote) n).getTitle().toLowerCase();
				if (this.hasKeywords(lowerCaseKeywords, content) || this.hasKeywords(lowerCaseKeywords, title)) {
					resultNotes.add(n);
				}
			} else if (n instanceof ImageNote) {
				String title = n.getTitle().toLowerCase();
				if (this.hasKeywords(lowerCaseKeywords, title)) resultNotes.add(n);
			}
		}

		return resultNotes;
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
	
	public void sortNotes() {
		this.notes.sort(null);
	}
	
	public boolean equals(Object folder) {
		Folder otherFolder = (Folder) folder;
		return otherFolder.getName().equals(this.name);
	}
	
	@Override
	public int compareTo(Folder f) {
		int result = f.getName().compareTo(this.name);
		if (result > 0) return 1;
		else if (result < 0) return -1;
		return 0;
	}
	
}
