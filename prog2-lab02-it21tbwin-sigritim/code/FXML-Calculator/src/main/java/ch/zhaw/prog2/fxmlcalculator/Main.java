package ch.zhaw.prog2.fxmlcalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * Main-Application. Opens the first window (MainWindow) and the common ValueHandler
 * @author
 * @version 1.0
 */
public class Main extends Application {

    private ValueHandler valueHandler;
    private MainWindowController controller;

    private HashMap<String, Parent> screens = new HashMap<String, Parent>();


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		valueHandler = new ValueHandler();
		openmainWindow(primaryStage);
	}

	private void openmainWindow(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Pane rootPane = loader.load();

            valueHandler = new ValueHandler();
            controller = loader.getController();
            controller.setValueHandler(valueHandler);

            Scene scene = new Scene(rootPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ROI Calculator");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

