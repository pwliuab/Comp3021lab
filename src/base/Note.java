package base;
import java.util.Date;
import java.util.Objects;
public class Note {
	private Date date;
	private String title;
	
	public String getTitle() {
		return this.title;
	}
		

	@Override
	public boolean equals(Object obj) {
		Note other = (Note) obj;
		return this.title.equals(other.getTitle());
	}
	
	

	public Note(String title) {
		this.title = title;
		date = new Date(System.currentTimeMillis());
	}
	
	
	
	
	

	
}
