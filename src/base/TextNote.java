package base;

import java.io.*;

public class TextNote extends Note implements java.io.Serializable {
    String content = "";

    public TextNote(String title) {
        super(title);
    }

    public TextNote(String title, String content) {
        super(title);
        this.content = content;
    }

    public TextNote(File f) throws IOException {
        super(f.getName());
        this.content = getTextFromFile(f.getAbsolutePath());
    }

    public String getTextFromFile(String absolutePath) throws IOException {
        String result = "";
        File filename = new File(absolutePath);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        while (line != null) {
            result += line;
            line = br.readLine();
        }
        return result;
    }

    public void exportTextToFile(String pathFolder) throws IOException {
        String fileTitle = this.getTitle();
        if (pathFolder == "") {
            pathFolder = ".";
        }
        while (fileTitle.contains(" ")) {
            fileTitle = fileTitle.replace(" ", "_");
        }
        File filename = new File(pathFolder + File.separator + fileTitle + ".txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename));
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(this.content);
        bw.close();
    }

    public String getContent() {
        return this.content;
    }
}
