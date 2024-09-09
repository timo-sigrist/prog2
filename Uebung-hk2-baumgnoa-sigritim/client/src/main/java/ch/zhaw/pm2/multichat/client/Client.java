package ch.zhaw.pm2.multichat.client;

import ch.zhaw.pm2.multichat.base.Config;
import javafx.application.Application;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the main starter of the client
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public class Client {
    /**
     * Main method starts the client ui and with that the whole client
     *
     * @param args
     */
    public static void main(String[] args) {
        Logger.getLogger(Config.CLIENT_LOGGER).log(Level.INFO, Config.LogMessage.CLIENT_START_MESSAGE.toString());
        Application.launch(ClientUI.class, args);
        Logger.getLogger(Config.CLIENT_LOGGER).log(Level.INFO, Config.LogMessage.CLIENT_END_MESSAGE.toString());
    }
}

