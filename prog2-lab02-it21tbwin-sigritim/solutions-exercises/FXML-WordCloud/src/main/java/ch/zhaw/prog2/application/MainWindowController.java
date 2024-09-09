package ch.zhaw.prog2.application;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainWindowController {
	private WordModelDecorator wordModelDecorator;

    @FXML
    private TextArea textHistory;

    @FXML
    private TextField textEingabe;

    @FXML
    private Label labelTitel;

	public void connectProperties() {
		//binding properties
		labelTitel.textProperty().bind(textEingabe.textProperty());		
	}
	
	public void setWordModel(WordModel wordModel) {
		wordModelDecorator = new WordModelDecorator(wordModel);
		wordModelDecorator.addListener(new IsObserver() {
			@Override
			public void update() {
				textHistory.setText(wordModel.toString());
			}
		});
	}

    @FXML
    private void hinzufuegenText(ActionEvent event) {
    	for(String word : textEingabe.getText().split(" ")) {
    		wordModelDecorator.addWord(word.toLowerCase());  		
    	}
    }

    @FXML
    private void leerenTextEingabe(ActionEvent event) {
    	textEingabe.clear();
    }

}

