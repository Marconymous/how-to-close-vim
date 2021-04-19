package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class Main extends Application {
    private TextArea textArea;
    private final TextArea lineCounter = new TextArea();
    private Label lines;
    private Label words;
    private Label nouns;
    private Label chars;
    private File file = null;
    private final FileHandler fh = new FileHandler();
    private int amtLines;

    @Override
    public void start(Stage primaryStage) {
        KeyCombination saveSC = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        KeyCombination saveAsSC = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN);

        VBox finalBox = new VBox();
        HBox menu = new HBox();
        HBox textPart = new HBox();
        HBox information = new HBox();

        // Save Button
        Button save = new Button("Save");
        save.setPrefSize(100, 20);
        save.setOnAction(e -> saveFile(primaryStage));

        // Load Button
        Button open = new Button("Open");
        open.setPrefSize(100, 20);
        open.setOnAction(e -> openFile(primaryStage));

        // Clear Button
        Button clear = new Button("Clear");
        clear.setPrefSize(100, 20);
        clear.setOnAction(e -> textArea.clear());

        // Author
        TextField author = new TextField();
        author.setPromptText("Author");

        menu.setSpacing(10);
        menu.getChildren().add(save);
        menu.getChildren().add(open);
        menu.getChildren().add(clear);
        menu.getChildren().add(author);

        // Line Counter
        lineCounter.setFont(Font.font("Consolas", 12));
        lineCounter.setPrefSize(50, Integer.MAX_VALUE);
        lineCounter.setDisable(true);
        lineCounter.setStyle("-fx-text-fill: white; -fx-control-inner-background:#000000;");

        // TextArea
        textArea = new TextArea();
        textArea.setFont(Font.font("Consolas", 12));
        textArea.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
        textArea.setPrefSize(textArea.getMaxWidth(), textArea.getMaxHeight());
        textArea.setStyle("-fx-text-fill: white; -fx-control-inner-background:#000000;");
        textArea.textProperty().addListener((text, oldVal, newVal) -> {
            updateInformation();
            updateLineCounter();
        });

        textArea.setOnScroll((event) -> {
            lineCounter.setScrollTop(textArea.getScrollTop());
        });

        // Text Part
        textPart.getChildren().add(lineCounter);
        textPart.getChildren().add(textArea);

        // File Information
        lines = new Label("Lines  : " + 0 + "\t");
        words = new Label("Words  : " + 0 + "\t");
        chars = new Label("Chars  : " + 0 + "\t");
        nouns = new Label("Nouns  : " + 0 + "\t");

        // final box
        information.getChildren().addAll(lines, words, nouns, chars);
        finalBox.getChildren().add(menu);
        finalBox.getChildren().add(textPart);
        finalBox.getChildren().add(information);
        finalBox.setSpacing(10);
        finalBox.setMinSize(300, 200);

        Scene root = new Scene(finalBox, 800, 500);

        // Shortcuts
        root.setOnKeyPressed(e -> {
            if (saveSC.match(e)) {
                saveFile(primaryStage);
            } else if (saveAsSC.match(e)) {
                setFile(primaryStage);
                fh.saveFile();
            }
        });

        primaryStage.setTitle("How do I close Vim | Editor");
        primaryStage.setScene(root);
        primaryStage.show();

        fh.setTextArea(textArea);
        fh.setAuthor(author);
    }

    private void updateLineCounter() {
        lineCounter.clear();
        for (int i = 1; i <= amtLines; i++) {
            lineCounter.appendText(i + "\n");
        }
    }

    private void openFile(Stage stage) {
        setFile(stage);
        fh.updateTextArea();
    }

    private void setFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Good Question!");
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Please select a file!");
            alert.setHeaderText("No File Selected");
            alert.setContentText("You need to select a file to open!");
            alert.showAndWait();
        } else {
            this.file = file;
            fh.setFile(this.file);
        }
    }

    private void updateInformation() {
        String[] inputText = textArea.getText().split("\n");
        amtLines = inputText.length;
        lines.setText("Lines  : " + amtLines + "\t\t");
        inputText = textArea.getText().split("[\\s]");
        words.setText("Words  : " + inputText.length + "\t");
        chars.setText("Length : " + textArea.getText().toCharArray().length + "\t");
        nouns.setText("Nouns  : " + getNouns() + "\t");
    }

    private int getNouns() {
        int count = 0;
        for (String s : textArea.getText().split("[\\s\\n]")) {
            if (s.length() >= 1) {
                if (s.toUpperCase().toCharArray()[0] == s.toCharArray()[0]) count++;
            }
        }
        return count;
    }

    private void saveFile(Stage stage) {
        if (this.file == null) saveFileChooser(stage);
        fh.saveFile();
    }

    private void saveFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Textfile", "*.txt"));
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.setInitialFileName(textArea.getText().split("\n")[0] + ".txt");
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Please select a file!");
            alert.setHeaderText("No File Selected");
            alert.setContentText("You need to select a file to open!");
            alert.showAndWait();
        } else {
            this.file = file;
            fh.setFile(this.file);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
