package ch.zhaw.prog2.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {

    WordModel model;
    MainWindowController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        openMainWindow(primaryStage);
    }

    private void openMainWindow(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/zhaw/prog2/fxmlcalculator/MainWindow.fxml"));
            Pane rootNode = loader.load();

            model = new WordModel();
            controller = loader.getController();
            controller.conectProperties();
            controller.setWordModel(model);


            Scene scene = new Scene(rootNode);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
