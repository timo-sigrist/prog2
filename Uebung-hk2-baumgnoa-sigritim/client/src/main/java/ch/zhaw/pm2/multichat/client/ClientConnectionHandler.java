package ch.zhaw.pm2.multichat.client;

import ch.zhaw.pm2.multichat.base.*;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client connection handler to handle sending, receiving and interpret messages on the client side
 * For every client a new thread of this class gets started
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public class ClientConnectionHandler extends ConnectionHandler {
    private final ChatWindowController controller;
    private final Logger logger = Logger.getLogger(Config.CLIENT_LOGGER);

    /**
     * Public constructor to get the actual connection and the username
     *
     * @param connection actual connection
     * @param userName   selected username
     */
    public ClientConnectionHandler(NetworkHandler.NetworkConnection<NetworkMessage> connection,
                                   String userName,
                                   ChatWindowController controller) {
        super(connection);
        setUserName((userName == null || userName.isBlank()) ? Config.UserMask.NONE.toString() : userName);
        this.controller = controller;
    }

    @Override
    public void setState(Config.State newState) {
        super.setState(newState);
        controller.handleStateChanged(newState);
    }

    /**
     * Initiates receiving data and handles the different exceptions
     */
    @Override
    protected void startReceiving() {
        logger.log(Level.INFO, Config.LogMessage.CLIENT_HANDLER_START_MESSAGE.toString());
        try {
            logger.log(Level.INFO, Config.LogMessage.HANDLER_RECEIVING_START_MESSAGE.toString());
            receiveAndProcessMessage();
            logger.log(Level.INFO, Config.LogMessage.HANDLER_RECEIVING_STOPED_MESSAGE.toString());
        } catch (SocketException e) {
            logger.log(Level.SEVERE, Config.LogMessage.HANDLER_CONNECTION_TERMINATED_LOCAL_ERROR.toString());
            this.setState(Config.State.DISCONNECTED);
            logger.log(Level.SEVERE, Config.LogMessage.CLIENT_CONNECTION_UNREGISTERED_ERROR.toString(), e.getMessage());
        } catch (EOFException e) {
            logger.log(Level.SEVERE, Config.LogMessage.HANDLER_CONNECTION_TERMINATED_ERROR.toString());
            this.setState(Config.State.DISCONNECTED);
            logger.log(Level.SEVERE, Config.LogMessage.CLIENT_CONNECTION_UNREGISTERED_ERROR.toString(), e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, Config.LogMessage.CLIENT_COMMUNICATION_ERROR.toString(), e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, Config.LogMessage.CLIENT_UNKNOWN_TYPE_ERROR.toString(), e.getMessage());
        }
        logger.log(Level.INFO, Config.LogMessage.CLIENT_CONNECTION_STOP_MESSAGE.toString());
    }

    /**
     * Closes the actual connection and handles possible exceptions
     */
    @Override
    public void stopReceiving() {
        logger.log(Level.INFO, Config.LogMessage.CLIENT_HANDLER_CLOSE_MESSAGE.toString());
        try {
            logger.log(Level.INFO, Config.LogMessage.HANDLER_RECEIVING_STOP_MESSAGE.toString());
            closeConnection();
            logger.log(Level.INFO, Config.LogMessage.HANDLER_RECEIVING_STOPED_MESSAGE.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, Config.LogMessage.HANDLER_CONNECTION_CLOSE_ERROR.toString(), e.getMessage());
        }
        logger.log(Level.INFO, Config.LogMessage.CLIENT_CONNECTION_CLOSE_MESSAGE.toString());
    }

    /**
     * Connects the client to the server
     *
     * @throws ChatProtocolException
     */
    void connect() throws ChatProtocolException {
        if (getState() != Config.State.NEW) {
            throw new ChatProtocolException(String.format(Config.LogMessage.CLIENT_CONNECT_STATE_ERROR.toString(), getState()));
        }
        sendNetworkMessage(
            new NetworkMessage(getUserName(),
                Config.UserMask.NONE.toString(),
                NetworkMessage.DataType.CONNECT,
                null));
        setState(Config.State.CONFIRM_CONNECT);
    }

    /**
     * Disconnects te client from the server
     *
     * @throws ChatProtocolException chat protocol exception
     */
    void disconnect() throws ChatProtocolException {
        if (getState() != Config.State.NEW && getState() != Config.State.CONNECTED) {
            throw new ChatProtocolException(String.format(Config.LogMessage.CLIENT_DISCONNECT_STATE_ERROR.toString(), getState()));
        }
        sendNetworkMessage(
            new NetworkMessage(getUserName(),
                Config.UserMask.NONE.toString(),
                NetworkMessage.DataType.DISCONNECT,
                null));
        setState(Config.State.CONFIRM_DISCONNECT);
    }

    /**
     * Sends a message to the server and with that to one or multiple receivers
     *
     * @param receiver receiver
     * @param message  message
     * @throws ChatProtocolException chat protocol exception
     */
    void sendMessage(String receiver, String message) throws ChatProtocolException {
        if (getState() != Config.State.CONNECTED) {
            throw new ChatProtocolException(String.format(Config.LogMessage.CLIENT_MESSAGE_STATE_ERROR.toString(), getState()));
        }
        sendNetworkMessage(
            new NetworkMessage(getUserName(),
                receiver,
                NetworkMessage.DataType.MESSAGE,
                message));
    }

    /**
     * Handles the datatype CONFIRM as input
     *
     * @param networkMessage network message
     */
    @Override
    protected void handleConfirmType(NetworkMessage networkMessage) {
        String receiver = networkMessage.getReceiver();
        String payload = networkMessage.getPayload();

        if (getState() == Config.State.CONFIRM_CONNECT) {
            setUserName(receiver);
            controller.setUserName(getUserName());
            controller.setServerPort(getConnection().getRemotePort());
            controller.setServerAddress(getConnection().getRemoteHost());
            controller.addInfo(payload);
            logger.log(Level.INFO, Config.LogMessage.CLIENT_CONFIRM_PAYLOAD_MESSAGE.toString(), payload);
            this.setState(Config.State.CONNECTED);
        } else if (getState() == Config.State.CONFIRM_DISCONNECT) {
            controller.addInfo(payload);
            logger.log(Level.INFO, Config.LogMessage.CLIENT_CONFIRM_PAYLOAD_MESSAGE.toString(), payload);
            this.setState(Config.State.DISCONNECTED);
        } else {
            logger.log(Level.WARNING, Config.LogMessage.CLIENT_UNEXPECTED_PAYLOAD_ERROR.toString(), payload);
        }
    }

    /**
     * Handles the datatype CONNECT as input
     * The client is not expecting a CONNECT message, so this is just an error message
     *
     * @param networkMessage network message
     */
    @Override
    protected void handleConnectType(NetworkMessage networkMessage) {
        logger.log(Level.WARNING, Config.LogMessage.CLIENT_ILLEGAL_CONNECT_REQUEST_MESSAGE.toString());
    }

    /**
     * Handles the datatype DISCONNECT as input
     *
     * @param networkMessage network message
     */
    @Override
    protected void handleDisconnectType(NetworkMessage networkMessage) {
        String payload = networkMessage.getPayload();

        if (getState() == Config.State.DISCONNECTED) {
            logger.log(Level.INFO, Config.LogMessage.CLIENT_ALREADY_DISCONNECTED_MESSAGE.toString(), payload);
        } else {
            controller.addInfo(payload);
            logger.log(Level.INFO, Config.LogMessage.CLIENT_DISCONNECT_PAYLOAD_MESSAGE.toString(), payload);
            this.setState(Config.State.DISCONNECTED);
        }
    }

    /**
     * Handles the datatype MESSAGE as input
     *
     * @param networkMessage network message
     */
    @Override
    protected void handleMessageType(NetworkMessage networkMessage) {
        if (getState() != Config.State.CONNECTED) {
            logger.log(Level.WARNING, Config.LogMessage.CLIENT_ILLEGAL_STATE_MESSAGE.toString(), new String[]{getState().toString(), networkMessage.getPayload()});
        } else {
            controller.addMessage(networkMessage);
            logger.log(Level.INFO, Config.LogMessage.CLIENT_SENT_MESSAGE.toString(), new String[]{networkMessage.getSender(), networkMessage.getReceiver(), networkMessage.getPayload()});
        }
    }

    /**
     * Handles the datatype ERROR as input
     *
     * @param networkMessage network message
     */
    @Override
    protected void handleErrorType(NetworkMessage networkMessage) {
        String payload = networkMessage.getPayload();

        controller.addError(payload);
        logger.log(Level.SEVERE, Config.LogMessage.CLIENT_ERROR_PAYLOAD_ERROR.toString(), payload);
    }
}
