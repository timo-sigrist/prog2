package ch.zhaw.pm2.arbeitsrapport.ui.textbausteineWindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TextbausteinWindowController {

    @FXML
    private TableView<?> tableView;

    @FXML
    void addText(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTextbausteinWindow.fxml"));
            Pane rootNode = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(rootNode);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("Window could not be loaded " + e.toString());
        }
    }

}
