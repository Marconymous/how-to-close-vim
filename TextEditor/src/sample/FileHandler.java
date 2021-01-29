package sample;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {
    private File file;
    private TextArea textArea;
    private TextField author;

    public FileHandler() {
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public void updateTextArea() {
        try {
            Scanner fileScanner = new Scanner(file);
            textArea.clear();
            int counter = 0;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.split(" ")[0].equalsIgnoreCase("@author") && (counter == 0)) {
                    author.setText(line.replaceAll("@author ", ""));
                    continue;
                } else {
                    textArea.appendText(line + "\n");
                }
                counter++;
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    public void saveFile() {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write("@author " + author.getText() + "\n");
            fw.write(textArea.getText());
            fw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void setAuthor(TextField author) {
        this.author = author;
    }
}
