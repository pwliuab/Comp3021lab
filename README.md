## COMP 3021 Interview Code 
>## Group 3 :
>##### https://github.com/pwliuab/Comp3021lab/tree/main/src/base

### Environment setup, IDE:
```
- OS : Windows 
- IDE: Eclipse
- Java version: javac -version,  javac 11.0.11
```
### Modified files/ Class:
```
- code : https://github.com/pwliuab/Comp3021lab/tree/main/src/base
- Modified Class
  - Folder.class
  - ImageNote.class
  - Note.class
  - NoteBook.class
  - TextNote.class
```
### Lab 4  toDoList
- [x] Save the object NoteBook to file
- [X] Load an object NoteBook from file
- [X] Import a TextNote from file
- [x] Export a TextNote to file 

## COMP 4021 Interview Code : Task 1
### Lab 4 Difficult Point
#### Task 1
```
Since this is quite clear in the lab description, the task required to do is to 
type implements to corresponding class and use "this" keyword to reperesent the current class.
----------------------------------------------
So the original provided code:
try {
fos = new FileOutputStream(“file.ser”);
out = new ObjectOutputStream(fos);
out.writeObject(object);
out.close();
} catch (Exception e) {
e.printStackTrace();
}
----------------------------------------------
Can be modified to:

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
---------------------------------------------
Description:
From the above code, it makes sense to use "this" to replace object,
Since we want to save current class's information into file.
The difficult point is we may not serialize the data member class,
which can be a potential compilation error.
```

#### Task 2
```
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
---------------------------------------------
Description:
this is not difficult, since file opening code is provided.
we need to understand type casting (NoteBook) although it is provided.
here we use (NoteBook) is to convert the inherited type of class to NoteBook,
in order to use NoteBook's function.
```
#### Task 3
```
Description: 
Since in this task, we are required to extract text from the file, 
it does not relate to what we learn in lecture. 

My initial thoguht is that there may exist 3 steps:
1. find and read file
2. extract data from file line by line
3. close file

Then I start googling possible solution : by searching keywords - how can I extract text form text file in Java
The reference website: https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java

```
#### Task 4
```
Since I have done the assignment 1, 
I know there may exist similar code that helps me export text file.
I copy codes there.
The difficult point is to figure out "./" which is the current directory.

```

## COMP 4021 Interview Code : Task 2
### Design Problem Group 3: 
#### Description:
```
Group3: 

We have some table data stored in .txt files. The format is like this:

name, major

Bowen, CS

Chengpeng, CS

The first row is several column names(table structure), and the following rows are table data. Note that there could be random number of columns. 

The goal is that we want to load such file into Java and create an object of class TableNote for it. The TableNote should contain the table structure and all the table data in it. In addition, it could also be stored back to .txt file. Describe your design of TableNote and how you load and store the data.
```
#### overview design
```
- 1. we need to create a class called TableNote which is a similar type to "note", so it can implement Note and implement serializable to store it into txt,
- 2. For this tableNote, since each row is a record, which row's data should be store together to keep their meaning. - like database,
     so the row content
     Each record should be accessed whenever we get a specific "column name" or by a series of condition,
     e.g. user enter : name = "Bowen", major = "CS", it returns specific record number.
          user enter : name = "CS", returns:  record 0, record 1...
- 3. For the Constructor, it takes String : file directory as a parameter, read and store content into the current class.
- 4. For the Method : it takes user command to return a list of record
```
#### possible data structure
```
![image](https://user-images.githubusercontent.com/67176560/161192285-0a24bb8d-4b37-4903-b50b-2b3178f8a9d9.png)

class TableNote implements  Note, {
  private ArrayList <HashMap<String, String>> tableList;
  private HashMap<String, HashMap<String,Set<Integer>>> columnMap;
  private ArrayList <String> titleList;

  public TableNote(String file) {
   ...
  }
  
  public setColumnMap(String columnTitle, String columnContent, int tableListRowNumber) {
    ....
  }
  
  public setTableList(String columnTitle, String content, int row) {
    ....
  }
  
  public ArrayList<HashMap<String, String>> getFileByCondition(String cmd) {
    ...
  }
  
  public boolean save(String file) {
    ...
  }
  public void getTextFromFile(String absolutePath) {
    ...
  }
  
}




- tableList: ArrayList <HashMap<String, String>>
  - HashMap<String, String> is to seperate a row into different column,
  - String :key is "column name", String :value is "content"
  
- columnMap: HashMap<String, HashMap<String,Set<Integer>>> columnMap;
  - HashMap<String, HashMap<String, Set<Integer>> >, is to store row number in tableList by      
    String Column
    e.g. if we want to find a set of record where Major = CS 
          following code : columnMap.get("Major").get("CS")
  - String: key is "column title name", e.g. "Major", 
    HashMap<String, Set<Integer>> : value, stores a set of row number that related to "talbeList",
- titleList: ArrayList <String>, for content's reference.
  e.g. if we split content String[] stringContent = "CS, Paul".split(", "),  stringContent[0] -> CS , titleList.get(0) -> Major
  
  

```
#### important algorithms
```
- String.split(",") to split String in a line, get different columns and contents
- while loop and for loop, loop through String array to assign data member
- set columnMap 

```
#### The new classes, method members may add and their functionalities
```
class TableNote implements  Note, {
  private ArrayList <HashMap<String, String>> tableList;
  private HashMap<String, Set<Integer>> columnMap;
  public TableNote(String file) {
   ...
  }
  public exportNoteToFile(String dir) {
    ....
  }
  
  public ArrayList<HashMap<String, String>> getFileByCondition(String cmd) {
    ...
  }
}
```
#### Implementation / source code
```
- github: 
---------------------------------------
package base;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class TableNote extends  Note implements java.io.Serializable {
	  private ArrayList <HashMap<String, String>> tableList;
	  private HashMap<String ,HashMap<String, Set<Integer>>> columnMap;
	  private ArrayList <String> titleList;
	  public TableNote(String filePath) {
		  super("Table Note ");
		  
		  try {
			  this.tableList = new ArrayList<>();
			  this.columnMap = new HashMap<>();
			  this.titleList = new ArrayList<>();
			  this.getTextFromFile(filePath);
//            uncomment it to test your data structure			  
//	        	this.testTableList(this.tableList);
//	        	this.testColumnMap();
		  } catch(Exception e) {
			  System.out.print(e);
		  }
		  
	  }
	  public TableNote(String filelocation, boolean isObjectFile) {
		  super("Table Note through Object file");
			FileInputStream fis = null;
			ObjectInputStream in = null;
			try {
				fis = new FileInputStream(filelocation);
				in = new ObjectInputStream(fis);
				TableNote n = (TableNote) in.readObject();
				this.tableList = n.tableList;
				this.columnMap = n.columnMap;
				this.titleList = n.titleList;
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	  }
	  
	  
	  public ArrayList<String> getRecord(String cmd) {
		  
		  if (cmd.equals("")) return null;
		  String [] constrainList = cmd.split(" and ");
		  HashMap<Integer, Integer> rowNumpairNumOfExistence = new HashMap<>();
		  for (String item : constrainList) {
			  String[] constrains = item.split(" = ");
			  String key = constrains[0];
			  String value = constrains[1];
			  Set<Integer> rowNumSet = this.columnMap.get(key).get(value);
			  for (Integer i : rowNumSet) {
				  if (rowNumpairNumOfExistence.containsKey(i)) {
					  rowNumpairNumOfExistence.put(i, rowNumpairNumOfExistence.get(i) + 1);
				  } else {
					  rowNumpairNumOfExistence.put(i, 1);
				  }
			  }
			  
		  }
		  
		  ArrayList<String> resultList = new ArrayList<>();
		  for (HashMap.Entry<Integer, Integer> record: rowNumpairNumOfExistence.entrySet()) {
			  if (constrainList.length == record.getValue()) {
				 String result = "";
				 HashMap<String, String> columnNamepairContent= this.tableList.get(record.getKey());
				 for (HashMap.Entry<String, String> content: columnNamepairContent.entrySet()) {
					 result += content.getKey();
					 result += " : ";
					 result += content.getValue();
					 result += ", ";
				 }
				 resultList.add(result);
				 System.out.println(result);
			  }
		  }
		  return resultList;
	  }
	  
	  public void setColumnMap(String columnTitle, String columnContent, int tableListRowNumber) {
		  Set<Integer> recordCollection = null;
		  if (this.columnMap.containsKey(columnTitle)) {
			  HashMap<String, Set<Integer>> recordList = this.columnMap.get(columnTitle);
			  if ( recordList.containsKey(columnContent)) {
				  recordCollection = recordList.get(columnContent);
				  recordCollection.add(tableListRowNumber);
			  } else {
				  recordCollection = new HashSet<Integer>();
				  recordCollection.add(tableListRowNumber);
				  recordList.put(columnContent, recordCollection);
			  }

			  return;
		  }
		  
		  recordCollection = new HashSet<Integer>();
		  recordCollection.add(tableListRowNumber);
		  HashMap<String, Set<Integer>> columnCstr = new HashMap<>();
		  columnCstr.put(columnContent, recordCollection);
		  this.columnMap.put(columnTitle, columnCstr);
	  }
	  
	  public void setTableList(String columnTitle, String content, int row) {
		  HashMap<String, String> record = null;
		  if (this.tableList.size() - 1 != row) {
			  record = this.tableList.get(this.tableList.size() - 1);
			  record.put(columnTitle, content);
			  return;
		  }
		  
		  record = new HashMap<>();
		  record.put(columnTitle, content);
		  this.tableList.add(record);
	  }
	  
	  public void setTitleList(String [] titleList) {
		  if(this.titleList == null) this.titleList = new ArrayList<String>();
		  for (String item: titleList) {
			  this.titleList.add(item);
		  }
	  }
	  
	  public void testTableList(ArrayList<HashMap<String, String>> tlist) {
		  for (HashMap<String, String> item : tlist) {
			  System.out.println("Row :  =================================");
			  for (HashMap.Entry<String, String> record : item.entrySet()) {
				  System.out.println("Column :" + record.getKey() + ", Content: " + record.getValue());
			  }
		  }
		  
	  }
	  
	  public void testColumnMap() {

		  for (HashMap.Entry<String, HashMap<String, Set<Integer>>> item : this.columnMap.entrySet()) {
			  System.out.println("Column Title : " + item.getKey());
			  for (HashMap.Entry<String, Set<Integer>> columnNamePairRowNumber: item.getValue().entrySet()) {
				  String rowNumberList = "";
				  System.out.println("column content: " + columnNamePairRowNumber.getKey());
				  for (int i : columnNamePairRowNumber.getValue()) {
					  rowNumberList += i + " ";
				  }
				  System.out.println("CONTENT LIST Row :" + rowNumberList);
			  }
		  }
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
	  
	  public void getTextFromFile(String absolutePath) throws IOException {
		File filename = new File(absolutePath);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        String [] columnNames = null;
        while (line != null) {
        	if (columnNames == null) {
        		columnNames = line.split(", ");
        		this.setTitleList(columnNames);
                line = br.readLine();
        		continue;
        	}
        	
        	String [] contentList = line.split(", ");
        	// set the tableList
        	int row = this.tableList.size() - 1;
        	for (int i = 0; i < contentList.length;  i++) {
        		this.setTableList(this.titleList.get(i), contentList[i], row);
        		this.setColumnMap(this.titleList.get(i), contentList[i], this.tableList.size() - 1);
        	}
        	

            line = br.readLine();
        }
        
        
	}
	  
	  
	  public void exportNoteToFile(String dir) {
		  
	  }
	  
//	  public ArrayList<HashMap<String, String>> getFileByCondition(String cmd) {
//		  
//	  }
	  
	}


```
