package base;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class NoteBook {
	private ArrayList<Folder> folders;
	
	public NoteBook() {
		this.folders = new ArrayList<Folder>();
	}
	
	public void sortFolders() {
		for (int i = 0; i < this.folders.size(); i ++) {
			this.folders.get(i).sortNotes();
		}
		
		Collections.sort(this.folders);
	}
	
	public boolean insertNote(String folderName, Note note) {
		Folder targetFolder = null;
		Folder newFolder = new Folder(folderName);
		// get targetfolder if exist
		for (Folder folder : this.folders) {
			if (newFolder.equals(folder)) targetFolder = folder; 
		}
		// check if we get the folder from list to targetFolder
		if (targetFolder == null) {
			this.folders.add(newFolder);
			targetFolder = newFolder;
		} else newFolder = null;
		
		ArrayList<Note> notes = targetFolder.getNotes();
		for (Note noteInFolder : notes) {
			if (noteInFolder.equals(note)) {
				System.out.println("Creating note " + note.getTitle() + " under folder " + folderName + " failed");
				return false;
			}
		}
		
		targetFolder.addNote(note);
		return true;
		
		
	}

	public List<Note>searchNotes(String keywords){
		List<Note> resultList = new ArrayList<Note>();
		for (Folder f : this.folders) {
			List<Note> searchedNotes = f.searchNotes(keywords);
			for (Note n : searchedNotes) {
				resultList.add(n);
				
			}
		}
		
		return resultList;
	}
	
	public boolean createTextNote(String folderName, String title, String content) {
		TextNote note = new TextNote(title, content);
		return insertNote(folderName, note);
	}
	
	public boolean createTextNote(String folderName, String title) {
		TextNote note = new TextNote(title);
		return this.insertNote(folderName, note);
	}
	
	public boolean createImageNote(String folderName, String title) {
		ImageNote note = new ImageNote(title);
		return this.insertNote(folderName, note);
	}
	
	public ArrayList<Folder> getFolders() {
		return new ArrayList<Folder>(this.folders);
	}
}
