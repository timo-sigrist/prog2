package ch.zhaw.pm2.multichat.base;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract upper class for connection handlers, which implements the runnable interface and also methods which
 * stay the same in every connection handler.
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public abstract class ConnectionHandler implements Runnable {
    private final NetworkHandler.NetworkConnection<NetworkMessage> connection;
    private String userName;
    private Config.State state;

    /**
     * Standard constructor which requires a connection
     *
     * @param connection connection
     */
    protected ConnectionHandler(NetworkHandler.NetworkConnection<NetworkMessage> connection) {
        this.connection = connection;
        state = Config.State.NEW;
    }

    /**
     * Gets run by threading framework
     */
    @Override
    public void run() {
        startReceiving();
    }

    /**
     * Sends a network message
     *
     * @param networkMessage network message
     */
    public void sendNetworkMessage(NetworkMessage networkMessage) {
        if (connection.isAvailable()) {
            try {
                connection.send(networkMessage);
            } catch (SocketException e) {
                Logger.getLogger("HandlerLogger").log(Level.SEVERE, Config.LogMessage.HANDLER_CONNECTION_CLOSED_ERROR.toString(), e.getMessage());
            } catch (EOFException e) {
                Logger.getLogger("HandlerLogger").log(Level.SEVERE, Config.LogMessage.HANDLER_CONNECTION_TERMINATED_ERROR.toString());
            } catch (IOException e) {
                Logger.getLogger("HandlerLogger").log(Level.SEVERE, Config.LogMessage.HANDLER_COMMUNICATION_ERROR.toString(), e.getMessage());
            }
        }
    }

    /**
     * Receives a network message and starts the data processing. This method gets called by the abstract method
     * startReceiving.
     *
     * @throws IOException            input output exception
     * @throws ClassNotFoundException class not found exception
     */
    protected void receiveAndProcessMessage() throws IOException, ClassNotFoundException {
        while (connection.isAvailable()) {
            processNetworkMessage(connection.receive());
        }
    }

    /**
     * Closes a connection. This method gets called by the abstract method stopReceiving.
     *
     * @throws IOException input output exception
     */
    protected void closeConnection() throws IOException {
        connection.close();
    }

    /**
     * Validates a message and handles the message input
     *
     * @param networkMessage network message
     */
    protected void processNetworkMessage(NetworkMessage networkMessage) {
        try {
            validateNetworkMessage(networkMessage);
            handleMessageInput(networkMessage);
        } catch (ChatProtocolException e) {
            Logger.getLogger("HandlerLogger").log(Level.SEVERE, Config.LogMessage.HANDLER_PROCESS_ERROR.toString(), e.getMessage());
            sendNetworkMessage(new NetworkMessage(Config.UserMask.NONE.toString(), userName, NetworkMessage.DataType.ERROR, e.getMessage()));
        }
    }

    /**
     * Handles a message input and calls the matching methods in the subclasses (abstract methods in this class)
     *
     * @param networkMessage network message
     * @throws ChatProtocolException chat protocol exception
     */
    protected synchronized void handleMessageInput(NetworkMessage networkMessage) {
        switch (networkMessage.getType()) {
            case CONFIRM -> handleConfirmType(networkMessage);
            case CONNECT -> handleConnectType(networkMessage);
            case DISCONNECT -> handleDisconnectType(networkMessage);
            case MESSAGE -> handleMessageType(networkMessage);
            case ERROR -> handleErrorType(networkMessage);
            default -> Logger.getLogger("HandlerLogger").log(Level.WARNING, Config.LogMessage.HANDLER_UNKNOWN_DATATYPE_ERROR.toString(), networkMessage.getType());
        }
    }

    /**
     * Validates the network message and throws an exception if it has a failure
     *
     * @param networkMessage networkmessage-object to validate
     * @throws ChatProtocolException chat protocol exception
     */
    private void validateNetworkMessage(NetworkMessage networkMessage) throws ChatProtocolException {
        if (networkMessage.getSender() == null) {
            throw new ChatProtocolException(Config.LogMessage.HANDLER_NO_SENDER_FOUND_ERROR.toString());
        }
        if (networkMessage.getReceiver() == null) {
            throw new ChatProtocolException(Config.LogMessage.HANDLER_NO_RECEIVER_FOUND_ERROR.toString());
        }
        if (networkMessage.getType() == null) {
            throw new ChatProtocolException(Config.LogMessage.HANDLER_NO_TYPE_FOUND_ERROR.toString());
        }
    }

    /**
     * Sets the username
     *
     * @param userName username
     */
    protected void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Sets the state
     *
     * @param state state
     */
    protected void setState(Config.State state) {
        this.state = state;
    }

    /**
     * Returns the username
     *
     * @return username
     */
    protected String getUserName() {
        return userName;
    }

    /**
     * Returns the state
     *
     * @return state
     */
    public Config.State getState() {
        return state;
    }

    /**
     * Returns the connection
     *
     * @return connection
     */
    protected NetworkHandler.NetworkConnection<NetworkMessage> getConnection() {
        return connection;
    }

    /**
     * Handles the start receiving which calls the receiveAndProcessMessage method
     */
    protected abstract void startReceiving();

    /**
     * Handles the stop receiving which calls the closeApplication method
     */
    public abstract void stopReceiving();

    /**
     * Handles the datatype CONFIRM as input
     *
     * @param networkMessage network message
     */
    protected abstract void handleConfirmType(NetworkMessage networkMessage);

    /**
     * Handles the datatype CONNECT as input
     *
     * @param networkMessage network message
     */
    protected abstract void handleConnectType(NetworkMessage networkMessage);

    /**
     * Handles the datatype DISCONNECT as input
     *
     * @param networkMessage network message
     */
    protected abstract void handleDisconnectType(NetworkMessage networkMessage);

    /**
     * Handles the datatype MESSAGE as input
     *
     * @param networkMessage network message
     */
    protected abstract void handleMessageType(NetworkMessage networkMessage);

    /**
     * Handles the datatype ERROR as input
     *
     * @param networkMessage network message
     */
    protected abstract void handleErrorType(NetworkMessage networkMessage);
}
