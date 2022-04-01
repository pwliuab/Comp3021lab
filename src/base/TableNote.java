package base;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
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
	  
	  
	    public void exportNoteToFile() throws IOException {
	        File filename = new File("data/result.txt");
	        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename));
	        BufferedWriter bw = new BufferedWriter(writer);
	        String content = "";
	        String titleContent = "";
	        boolean hasSetedTitle = false;
			  for (HashMap<String, String> item : this.tableList) {
				  System.out.println("Row :  =================================");
				  for (HashMap.Entry<String, String> record : item.entrySet()) {
					  System.out.println("Column :" + record.getKey() + ", Content: " + record.getValue());
					  if (!hasSetedTitle) {
						  titleContent += record.getKey();
						  titleContent += ", ";
					  }
					  content += record.getValue();
					  content += ", ";
				  }
				  
				  titleContent += "\n";
				  
				  if (!hasSetedTitle) bw.write(titleContent);
				  content += "\n";
				  hasSetedTitle = true;
				  bw.write(content);
			  }
			  
		  
//	        for (Record record : Records) {
//	            //Invoke the toString method of Record.
//	            bw.write(record.toString() + "\n");
//	        }
	        bw.close();
	    }
	  
//	  public ArrayList<HashMap<String, String>> getFileByCondition(String cmd) {
//		  
//	  }
	  
	}
