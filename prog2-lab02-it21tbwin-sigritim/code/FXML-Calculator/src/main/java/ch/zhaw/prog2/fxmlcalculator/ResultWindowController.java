package ch.zhaw.prog2.fxmlcalculator;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Controller for the MainWindow. One controller per mask (or FXML file)
 * Contains everything the controller has to reach in the view (controls)
 * and all methods the view calls based on events.
 * @author
 * @version 1.0
 */
public class ResultWindowController {

	@FXML
    private TextArea results;

	@FXML
	private void closeWindow() {
		Stage stage = (Stage) results.getScene().getWindow();
		stage.close();
	}

    public void bindResults (StringProperty results) {
        this.results.textProperty().bind(results);
    }

}
