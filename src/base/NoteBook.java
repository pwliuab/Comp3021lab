package base;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
public class NoteBook implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Folder> folders;
	
	public NoteBook() {
		this.folders = new ArrayList<Folder>();
	}
	
	public NoteBook(String file) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			NoteBook n = (NoteBook) in.readObject();
			this.folders = n.getFolders();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void sortFolders() {
		for (int i = 0; i < this.folders.size(); i ++) {
			this.folders.get(i).sortNotes();
		}
		
		Collections.sort(this.folders);
	}
	
	public boolean save(String file) {
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
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
