package ch.zhaw.prog2.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainWindowController {

    @FXML
    private Label labelTitle;

    @FXML
    private TextField textField;

    @FXML
    private Button buttonAddText;

    @FXML
    private Button buttonDeleteField;

    @FXML
    private TextArea textArea;

    private WordModelDecorator wordModelDecorator;

    public void conectProperties() {
        labelTitle.textProperty().bind(textField.textProperty());
    }

    public void setWordModel(WordModel wordModel) {
        wordModelDecorator = new WordModelDecorator(wordModel);
        wordModelDecorator.addListener(new IsObserver() {
            @Override
            public void update() {
                textArea.setText(wordModel.toString());
            }
        });
    }

    @FXML
    private void addText(ActionEvent event) {
        wordModelDecorator.addWord(textField.getText());
    }

    @FXML
    private void deleteField(ActionEvent event) {
        textField.clear();
    }

}
