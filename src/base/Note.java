package base;
import java.util.Date;
import java.util.Objects;
public class Note implements Comparable<Note> {
	private Date date;
	private String title;
	
	public String getTitle() {
		return this.title;
	}
	
	public String toString() {
		return date.toString() + "\t" + title;
	}
	
	@Override
	public int compareTo(Note o) {
		if (this.date.compareTo(o.date) > 0) return 1;
		else if (this.date.compareTo(o.date) < 0) return -1;
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		Note other = (Note) obj;
		return this.title.equals(other.getTitle());
	}
	
	

	public Note(String title) {
		this.title = title;
		this.date = new Date(System.currentTimeMillis());
	}
	
	
	
	
	

	
}
