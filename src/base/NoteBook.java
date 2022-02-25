package base;
import java.util.ArrayList;
public class NoteBook {
	private ArrayList<Folder> folders;
	
	public NoteBook() {
		this.folders = new ArrayList<Folder>();
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
