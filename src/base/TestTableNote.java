package base;

import java.io.File;

public class TestTableNote {
	public static void main(String args[]) throws Exception{
		TableNote tableList = new TableNote("data/Hospital.txt");
		tableList.getRecord("Name = Paul and Major = CS");
		tableList.save("gg.ser");
		tableList.exportNoteToFile();
		TableNote objectFromSerFile = new TableNote("gg.ser", true);
		objectFromSerFile.getRecord("Name = Ben");
	}
}
