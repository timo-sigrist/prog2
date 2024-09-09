package ch.zhaw.pm2.multichat.server;

import ch.zhaw.pm2.multichat.base.Config;
import ch.zhaw.pm2.multichat.base.NetworkHandler;
import ch.zhaw.pm2.multichat.base.NetworkMessage;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a server which stores the connections to the clients
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public class Server {
    private final NetworkHandler.NetworkServer<NetworkMessage> networkServer;
    private final Map<String, ServerConnectionHandler> connectionRegistry = new HashMap<>();
    private final ExecutorService executorService;
    private final Logger logger = Logger.getLogger(Config.SERVER_LOGGER);

    /**
     * Public constructor which requires the server port
     *
     * @param serverPort server port
     * @throws IOException input output exception
     */
    public Server(int serverPort) throws IOException {
        logger.log(Level.INFO, Config.LogMessage.SERVER_CREATE_CONNECTION_MESSAGE.toString());
        networkServer = NetworkHandler.createServer(serverPort);
        logger.log(Level.INFO, Config.LogMessage.SERVER_CREATED_MESSAGE.toString(), new String[]{networkServer.getHostAddress(), Integer.toString(networkServer.getHostPort())});
        executorService = Executors.newCachedThreadPool();
    }

    /**
     * Starts the server
     */
    void start() {
        logger.log(Level.INFO, Config.LogMessage.SERVER_STARTED_MESSAGE.toString());
        try {
            while (true) {
                NetworkHandler.NetworkConnection<NetworkMessage> connection = networkServer.waitForConnection();
                ServerConnectionHandler connectionHandler = new ServerConnectionHandler(connection, connectionRegistry);
                executorService.execute(connectionHandler);
                logger.log(Level.INFO, Config.LogMessage.SERVER_CONNECTED_MESSAGE.toString(), new String[]{connection.getRemoteHost(), Integer.toString(connection.getRemotePort())});
            }
        } catch (SocketException e) {
            logger.log(Level.SEVERE, Config.LogMessage.SERVER_CONNECTION_TERMINATED_MESSAGE.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, Config.LogMessage.SERVER_COMMUNICATION_ERROR.toString(), e);
        }
        logger.log(Level.INFO, Config.LogMessage.SERVER_STOPPED_MESSAGE.toString());
    }

    /**
     * Sends a close message to the client and closes the connection to the client
     */
    private void closeConnections() {
        Iterator<Map.Entry<String, ServerConnectionHandler>> iterator =
            connectionRegistry.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, ServerConnectionHandler> entry = iterator.next();
            String userName = entry.getKey();
            ServerConnectionHandler connectionHandler = entry.getValue();

            connectionHandler.sendNetworkMessage(
                new NetworkMessage(userName,
                    Config.UserMask.NONE.toString(),
                    NetworkMessage.DataType.DISCONNECT,
                    String.format(Config.UIMessage.SERVER_CLOSED_MESSAGE_TO_CLIENTS.toString(), userName))
            );
            connectionHandler.stopReceiving();

            iterator.remove();
        }
    }

    /**
     * Terminate the network and shuts down the executor service
     */
    private void terminate() {
        try {
            logger.log(Level.INFO, Config.LogMessage.SERVER_START_CLOSE_MESSAGE.toString());
            networkServer.close();
            executorService.shutdown();
        } catch (IOException e) {
            logger.log(Level.SEVERE, Config.LogMessage.SERVER_CLOSED_ERROR_MESSAGE.toString(), e);
        }
    }

    void shutdown() {
        closeConnections();
        terminate();
    }
}
