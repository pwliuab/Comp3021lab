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
class TableNote implements  Note, {
  private ArrayList <HashMap<String, String>> tableList;
  private HashMap<String, Set<Integer>> columnMap;
  ...
}
- tableList: @Generic Type HashMap<String, String>
  - HashMap<String, String> is to seperate a row into different column,
  - String :key is "column name", String :value is "content"
- columnMap:  HashMap<String, Set<Integer>> columnMap;
  - HashMap<String, HashMap<String, Set<Integer>> >, is to store row number in tableList by      
    String Column
    e.g. if we want to find a set of record where Major = CS 
          following code : columnMap.get("Major").get("CS")
  - String: key is "column name", 
    
```
#### important algorithms
```
- String.split() to split String in a line, get different columns and contents
```
#### The new classes, method members may add and their functionalities
```
```
