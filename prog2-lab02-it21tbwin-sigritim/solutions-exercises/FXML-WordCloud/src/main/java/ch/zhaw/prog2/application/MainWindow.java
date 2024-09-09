package ch.zhaw.prog2.application;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainWindow extends Application {
	
	private WordModel wordModel = new WordModel();

	@Override
	public void start(Stage primaryStage) throws Exception {
		openMainWindow(primaryStage);
	}
	
	private void openMainWindow(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));

            Pane rootNode = loader.load();

 			MainWindowController mainWindowController = loader.getController();
			mainWindowController.connectProperties();
			mainWindowController.setWordModel(wordModel);

			Scene scene = new Scene(rootNode);

            stage.setScene(scene);
            stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
