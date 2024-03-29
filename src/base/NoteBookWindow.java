package base;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * NoteBook GUI with JAVAFX
 * <p>
 * COMP 3021
 *
 * @author valerio
 */
public class NoteBookWindow extends Application {

    /**
     * TextArea containing the note
     */
    final TextArea textAreaNote = new TextArea("");
    /**
     * list view showing the titles of the current folder
     */
    final ListView<String> titleslistView = new ListView<String>();
    /**
     * Combobox for selecting the folder
     */
    final ComboBox<String> foldersComboBox = new ComboBox<String>();
    /**
     * This is our Notebook object
     */
    NoteBook noteBook = null;

    TextNote currentNote = null;


    /**
     * current folder selected by the user
     */
    String currentFolder = "";
    /**
     * current search string
     */
    String currentSearch = "";

    Stage stage;
    private Button SearchBtn = null;

    private Button ClearBtn = null;

    private TextField searchText = null;

    public static void main(String[] args) {
        launch(NoteBookWindow.class, args);
    }


    @Override
    public void start(Stage stage) {
        loadNoteBook();
        // Use a border pane as the root for scene
        BorderPane border = new BorderPane();
        // add top, left and center
        border.setTop(addHBox());
        border.setLeft(addVBox());
        border.setCenter(setvBox());

        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("NoteBook COMP 3021");
        stage.show();
        this.stage = stage;
    }


    private void updateFolderView() {
        ObservableList<String> Items = foldersComboBox.getItems();
        Items.clear();
        for (Folder folder : this.noteBook.getFolders()) {
            Items.add(folder.getName());
        }
    }    // call NoteBook load function

    private void loadNoteBook(File file) {
        String filePath = file.getAbsolutePath();
        this.noteBook = new NoteBook(filePath);
        updateFolderView();
        updateListView();

        System.out.println(filePath);
    }


    private File selectFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter("Serialized Object", "*.ser");
        fileChooser.getExtensionFilters().add(exFilter);
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }

    /**
     * This create the top section
     *
     * @return
     */
    private HBox addHBox() {
        int ButtonWidth = 100;
        int ButtonHeight = 30;
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10); // Gap between nodes

        Button buttonLoad = new Button("Load");
        buttonLoad.setPrefSize(ButtonWidth, ButtonHeight);
        buttonLoad.setDisable(false);
        Button buttonSave = new Button("Save");
        buttonSave.setPrefSize(ButtonWidth, ButtonHeight);
        buttonSave.setDisable(false);

        Label searchLabel = new Label("Search :");
        searchLabel.setPrefSize(50, 30);

        TextField searchTextField = new TextField("123");
        searchTextField.setPrefSize(100, 30);
        System.out.println(searchTextField.getText());
        searchText = searchTextField;


        Button buttonSearch = new Button("Search");
        buttonSearch.setPrefSize(ButtonWidth, ButtonHeight);
        buttonSearch.setDisable(false);

        Button buttonClear = new Button("Clear Search");
        buttonClear.setPrefSize(ButtonWidth, ButtonHeight);
        buttonClear.setDisable(false);


        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = selectFile("Please select a file to save");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully saved");
                alert.setContentText("Your file has been saved to file"
                        + file.getName()
                );
                noteBook.save(file.getAbsolutePath());
                currentFolder = "";
                currentNote = null;
                updateFolderView();
                updateListView();
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Press OK");
                    }
                });
            }
        });


        buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = selectFile("Please Choose An File Which Contains a NoteBook Object");
                if (file != null) {
                    // TO DO
                    loadNoteBook(file);
                    currentFolder = "";
                    currentNote = null;
                    updateFolderView();
                    updateListView();
                }
            }
        });


        // clear button's function
        buttonClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentSearch = "";
                searchTextField.setText("");
                if (textAreaNote != null) textAreaNote.setText("");
                System.out.print(currentFolder);
                if (currentFolder.equals("")) return;


                Folder f = findFolder(currentFolder);

                if (f == null) return;

                String titles = "";

                for (Note n : f.getNotes()) {
                    titles += n.getTitle();
                    titles += "\n";
                }


                textAreaNote.setText(titles);
            }
        });

        buttonSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentSearch = searchTextField.getText();
                if (textAreaNote != null) textAreaNote.setText("");

                String titles = "";
                Folder currFolder = findFolder(currentFolder);

                if (currFolder == null) return;

                List<Note> notes = currFolder.searchNotes(currentSearch);

                if (notes == null) return;

                for (Note note : notes) {
                    titles += note.getTitle();
                    titles += "\n";
                }


                if (textAreaNote != null) textAreaNote.setText(titles);
            }
        });


        hbox.getChildren().addAll(buttonLoad, buttonSave, searchLabel, searchTextField, buttonSearch, buttonClear);

        return hbox;
    }

    /**
     * this create the section on the left
     *
     * @return
     */

    public void addFolder(String fodlerName) {
        ArrayList<Folder> folders = this.noteBook.getFolders();
        folders.add(new Folder(fodlerName));
    }

    public Folder findFolder(String folderName) {
        for (Folder f : this.noteBook.getFolders()) {
            if (f.getName().equals(folderName)) return f;
        }
        return null;
    }

    private Boolean isExistFolder(String nfolder) {
        ArrayList<Folder> folders = noteBook.getFolders();
        for (Folder f : folders) {
            if (nfolder.equals(f.getName())) return true;
        }
        return false;
    }

    public Note findNote(Folder f, String title) {
        for (Note n : f.getNotes()) {
            if (n.getTitle().equals(title)) return n;
        }
        return null;
    }

    private void displayWarningMessage(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(content);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Press OK");
            }
        });
    }

    private void displayMessageAndRemoveNote(String content, Alert.AlertType icon, String title) {
        Alert alert = new Alert(icon);
        alert.setTitle("Message");
        alert.setContentText(content);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                this.removeNotes(title);
                int index = 0;
                for (String i : this.titleslistView.getItems()) {
                    if (i.equals(title)) break;
                    index++;
                }
                ObservableList<String> list = this.titleslistView.getItems();
                list.remove(index);
                this.titleslistView.setItems(list);
                this.textAreaNote.setText("");
            }
        });
    }

    public boolean removeNotes(String title) {

        Folder f = this.findFolder(currentFolder);
        int i = 0;
        for (Note n : f.getNotes()) {
            if (n.getTitle().equals(title)) {
                f.getNotes().remove(i);
                return true;
            }
            i++;
        }

        return false;
    }


    private void displayMessage(String content, String title, Alert.AlertType icon) {
        Alert alert = new Alert(icon);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Press OK");
            }
        });
    }

    private void displayMessage(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setContentText(content);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Press OK");
            }
        });
    }

    private VBox addVBox() {

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8); // Gap between nodes

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.setSpacing(10);

        Button AddFolderButton = new Button("Add a Folder");
        AddFolderButton.setPrefSize(100, 22);
        AddFolderButton.setDisable(false);

        Button AddNoteButton = new Button("Add a Note");
        AddNoteButton.setPrefSize(100, 22);
        AddNoteButton.setDisable(false);

        // TODO: This line is a fake folder list. We should display the folders in noteBook variable! Replace this with your implementation
//		for (noteBook.getFolders())
        ObservableList<String> Items = foldersComboBox.getItems();
        for (Folder folder : this.noteBook.getFolders()) {
            Items.add(folder.getName());
        }


        foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (t1 == null) return;
                currentFolder = t1.toString();
                // this contains the name of the folder selected
                // TODO update listview
                if (t == null) {
                    updateListView();
                    return;
                }
                if (!t1.toString().equals(t.toString())) updateListView();

            }

        });

        foldersComboBox.setValue("-----");

        titleslistView.setPrefHeight(100);


        titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (t1 == null)
                    return;
                String title = t1.toString();
                // This is the selected title
                // TODO load the content of the selected note in
                // textAreNote
                System.out.println(t1.toString());
                Folder folder = findFolder(currentFolder);

                if (folder == null) return;
                TextNote n = null;
                try {
                    n = (TextNote) findNote(folder, title);
                } catch (Exception e) {
                    textAreaNote.setText("");
                    currentNote = null;
                }


                if (n == null) return;
                currentNote = n;
                textAreaNote.setText(n.getContent());

            }
        });

        // event handler

        AddFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog diaog = new TextInputDialog("Add a Folder");
                diaog.setTitle("Input");
                diaog.setHeaderText("Add a new folder for your notebook:");
                diaog.setContentText("Please eneter the name you want to create:");

                Optional<String> result = diaog.showAndWait();

                if (result.isPresent()) {
                    // TO DO part
                    String folderName = diaog.getEditor().getText();
                    // checking
                    if (folderName.equals("")) {
                        displayWarningMessage("Please input a valid folder name");
                        return;
                    }
                    if (isExistFolder(folderName)) {
                        displayWarningMessage("We already have a folder named with " + folderName);
                        return;
                    } else addFolder(folderName);
                    // update  view
                    Items.add(folderName);
                }
            }
        });

        AddNoteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentFolder.equals("")) {

                    displayWarningMessage("Please choose a folder!");
                    return;
                }
                TextInputDialog diaog = new TextInputDialog("Add a Folder");
                diaog.setTitle("Input");
                diaog.setHeaderText("Add a new folder for your notebook:");
                diaog.setContentText("Please eneter the name you want to create:");

                Optional<String> result = diaog.showAndWait();

                if (result.isPresent()) noteBook.createTextNote(currentFolder, diaog.getEditor().getText());
                ObservableList<String> items = titleslistView.getItems();
                items.add(diaog.getEditor().getText());
                titleslistView.setItems(items);
                displayMessage("Insert note Lab8 JavaFx folder Lab successfully");

            }
        });
        //...
        hbox.getChildren().add(foldersComboBox);
        hbox.getChildren().add(AddFolderButton);

        vbox.getChildren().add(new Label("Choose folder: "));
        vbox.getChildren().add(hbox);
        vbox.getChildren().add(new Label("Choose note title"));
        vbox.getChildren().add(titleslistView);
        vbox.getChildren().add(AddNoteButton);

        return vbox;
    }


    private void updateListView() {
        ArrayList<String> list = new ArrayList<String>();

        // TODO populate the list object with all the TextNote titles of the
        // currentFolder

        ObservableList<String> combox2 = FXCollections.observableArrayList(list);
        titleslistView.setItems(combox2);
        ArrayList<String> textNoteTitles = new ArrayList<>();
        Folder currFolderObj = null;
        if (this.noteBook == null) return;
        if (this.noteBook.getFolders() == null) return;
        for (Folder f : this.noteBook.getFolders()) {
            if (f.getName().equals(this.currentFolder)) {
                currFolderObj = f;
                break;
            }
        }
        if (currFolderObj == null) return;

        for (Note note : currFolderObj.getNotes()) {
            textNoteTitles.add(note.getTitle());
        }

        ObservableList<String> items = FXCollections.observableArrayList();

        for (String textNoteTitle : textNoteTitles) {
            items.add(textNoteTitle);
        }
        this.titleslistView.setItems(items);
    }

    /*
     * Creates a grid for the center region with four columns and three rows
     */

    private ImageView displayIcon(String imageFile) {
        ImageView View = new ImageView(new Image((imageFile)));
        View.setFitHeight(18);
        View.setFitWidth(18);
        View.setPreserveRatio(true);
        return View;
    }

    private VBox setvBox() {
        HBox hbox = new HBox();
        ///                             bottom   left
        hbox.setPadding(new Insets(10, 10, 15, 10));
        hbox.setSpacing(10); // Gap between nodes

        ImageView saveView = this.displayIcon("C:\\Users\\paul\\git\\Comp3021lab\\src\\base\\save.png");

        Button buttonSave = new Button("Save");
        buttonSave.setPrefSize(100, 22);
        buttonSave.setDisable(false);

        ImageView deleteView = this.displayIcon("C:\\Users\\paul\\git\\Comp3021lab\\src\\base\\delete.png");

        Button buttonDelete = new Button("Delete");
        buttonDelete.setPrefSize(100, 22);
        buttonDelete.setDisable(false);


        // event handling
        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentFolder.equals("") || currentNote == null)
                    displayWarningMessage("Please select a folder and a note!");

                currentNote.content = textAreaNote.getText();


            }
        });

        buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentFolder.equals("") || currentNote == null) {
                    displayWarningMessage("Please select a folder and a note!");
                    return;
                }

                displayMessageAndRemoveNote("Are you sure?", Alert.AlertType.WARNING, currentNote.getTitle());

                displayMessage("You have successfully delete the note", "Succeed!", Alert.AlertType.CONFIRMATION);
                currentNote = null;


            }
        });

        // ...


        hbox.getChildren().add(saveView);
        hbox.getChildren().add(buttonSave);

        hbox.getChildren().add(deleteView);
        hbox.getChildren().add(buttonDelete);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(5)); // Set all sides to 10
        vbox.setSpacing(3);

        vbox.getChildren().add(hbox);
        vbox.getChildren().add(addGridPane());

        return vbox;
    }

    private GridPane addGridPane() {

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        textAreaNote.setEditable(false);
        textAreaNote.setMaxSize(450, 400);
        textAreaNote.setWrapText(true);
        textAreaNote.setPrefWidth(450);
        textAreaNote.setPrefHeight(400);


        // lab 8
        textAreaNote.setEditable(true);
        // 0 0 is the position in the grid

        grid.add(textAreaNote, 0, 0);

        return grid;
    }

    private void loadNoteBook() {
        NoteBook nb = new NoteBook();
        nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
        nb.createTextNote("COMP3021", "course information",
                "Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
        nb.createTextNote("COMP3021", "Lab requirement",
                "Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

        nb.createTextNote("Books", "The Throwback Special: A Novel",
                "Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called â€œthe most shocking play in NFL historyâ€� and the Washington Redskins dubbed the â€œThrowback Specialâ€�: the November 1985 play in which the Redskinsâ€™ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
        nb.createTextNote("Books", "Another Brooklyn: A Novel",
                "The acclaimed New York Times bestselling and National Book Awardâ€“winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everythingâ€”until it wasnâ€™t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliantâ€”a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwetherâ€™s Daddy Was a Number Runner and Dorothy Allisonâ€™s Bastard Out of Carolina, Jacqueline Woodsonâ€™s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthoodâ€”the promise and peril of growing upâ€”and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

        nb.createTextNote("Holiday", "Vietnam",
                "What I should Bring? When I should go? Ask Romina if she wants to come");
        nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
        nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
        noteBook = nb;

    }

}
