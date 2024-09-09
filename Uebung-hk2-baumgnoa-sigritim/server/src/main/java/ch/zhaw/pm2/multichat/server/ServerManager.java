package ch.zhaw.pm2.multichat.server;

import ch.zhaw.pm2.multichat.base.Config;
import ch.zhaw.pm2.multichat.base.NetworkHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server manager starts one or multiple servers
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public class ServerManager {

    private final Map<Integer, Server> servers;

    /**
     * Server main method starts one server. If an argument is passed it creates a server with the port which comes
     * with the argument. Has to be changed to create multiple servers.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        ServerManager serverManager = new ServerManager();
        try {
            if (args.length == 0) {
                serverManager.initiateServer(NetworkHandler.DEFAULT_PORT);
            } else {
                int port = Integer.parseInt(args[1]);
                serverManager.initiateServer(port);
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(Config.SERVER_LOGGER).log(Level.SEVERE, Config.LogMessage.SERVER_ILLEGAL_ARGUMENTS_MESSAGE.toString());
        } catch (IOException e) {
            Logger.getLogger(Config.SERVER_LOGGER).log(Level.SEVERE, Config.LogMessage.SERVER_START_ERRORMESSAGE.toString(), e.getMessage());
        }
    }

    /**
     * Public constructor creates the server manager
     */
    public ServerManager() {
        servers = new HashMap<>();
    }

    /**
     * Creates, saves and starts a server
     *
     * @param port selected port
     * @throws IOException input output exception
     */
    private void initiateServer(int port) throws IOException {
        try {
            Server server = new Server(port);
            servers.put(port, server);

            addShutdownHook(server);

            server.start();
        } catch (IOException e) {
            Logger.getLogger(Config.SERVER_LOGGER).log(Level.SEVERE, Config.LogMessage.SERVER_START_ERRORMESSAGE.toString(), e.getMessage());
        }
    }

    /**
     * Adds handling for the server shutdown
     *
     * @param server server
     */
    private void addShutdownHook(Server server) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    Logger.getLogger(Config.SERVER_LOGGER).log(Level.INFO, Config.LogMessage.SERVER_SHUTDOWN_START_MESSAGE.toString());
                    server.shutdown();
                } catch (InterruptedException e) {
                    Logger.getLogger(Config.SERVER_LOGGER).log(Level.WARNING, Config.LogMessage.SERVER_SHUTDOWN_INTERRUPTED_ERROR.toString(), e);
                } finally {
                    Logger.getLogger(Config.SERVER_LOGGER).log(Level.INFO, Config.LogMessage.SERVER_SHUTDOWN_COMPLETE_MESSAGE.toString());
                }
            }
        });
    }
}
