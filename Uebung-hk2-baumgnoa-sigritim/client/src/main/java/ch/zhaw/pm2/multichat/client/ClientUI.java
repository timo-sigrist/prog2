package ch.zhaw.pm2.multichat.client;

import ch.zhaw.pm2.multichat.base.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a client ui which gets started by start
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public class ClientUI extends Application {

    /**
     * Starts the JavaFX Application
     *
     * @param primaryStage primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        openChatWindow(primaryStage);
    }

    /**
     * Opens the chat window
     *
     * @param primaryStage
     */
    private void openChatWindow(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatWindow.fxml"));
            Pane rootPane = loader.load();
            Scene scene = new Scene(rootPane);

            primaryStage.setScene(scene);
            primaryStage.setMinWidth(420);
            primaryStage.setMinHeight(250);
            primaryStage.setTitle(Config.CLIENT_UI_TITLE);
            primaryStage.show();
        } catch (Exception e) {
            Logger.getLogger(Config.CLIENT_LOGGER).log(Level.SEVERE, Config.LogMessage.CLIENT_UI_START_ERROR.toString(), e.getMessage());
        }
    }
}
