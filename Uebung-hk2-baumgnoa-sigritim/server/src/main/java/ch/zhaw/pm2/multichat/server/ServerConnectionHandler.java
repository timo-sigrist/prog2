package ch.zhaw.pm2.multichat.server;

import ch.zhaw.pm2.multichat.base.Config;
import ch.zhaw.pm2.multichat.base.ConnectionHandler;
import ch.zhaw.pm2.multichat.base.NetworkHandler;
import ch.zhaw.pm2.multichat.base.NetworkMessage;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server connection handler to handle sending, receiving and interpret messages on the server side
 * For every client connection a new thread of this class gets started
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public class ServerConnectionHandler extends ConnectionHandler {
    private static final AtomicInteger connectionCounter = new AtomicInteger(0);
    private final int connectionId = connectionCounter.incrementAndGet();
    private final Map<String, ServerConnectionHandler> connectionRegistry;
    private final Logger logger = Logger.getLogger(Config.SERVER_LOGGER);


    /**
     * Public constructor to get the actual connection and the whole connection registry
     *
     * @param connection         actual connection
     * @param connectionRegistry map with all connections and their users
     */
    public ServerConnectionHandler(NetworkHandler.NetworkConnection<NetworkMessage> connection,
                                   Map<String, ServerConnectionHandler> connectionRegistry) {
        super(connection);
        this.connectionRegistry = connectionRegistry;
    }

    /**
     * Initiates receiving data and handles the different exceptions
     */
    @Override
    protected void startReceiving() {
        logger.log(Level.INFO, Config.LogMessage.SERVER_HANDLER_START_MESSAGE.toString());
        try {
            logger.log(Level.INFO, Config.LogMessage.HANDLER_RECEIVING_START_MESSAGE.toString());
            receiveAndProcessMessage();
            logger.log(Level.INFO, Config.LogMessage.HANDLER_RECEIVING_STOPED_MESSAGE.toString());
        } catch (SocketException e) {
            logger.log(Level.SEVERE, Config.LogMessage.HANDLER_CONNECTION_TERMINATED_LOCAL_ERROR.toString());
            connectionRegistry.remove(getUserName());
            logger.log(Level.SEVERE, Config.LogMessage.SERVER_CONNECTION_TERMINATED_ERROR.toString(), new String[]{getUserName(), e.getMessage()});
        } catch (EOFException e) {
            logger.log(Level.SEVERE, Config.LogMessage.HANDLER_CONNECTION_TERMINATED_ERROR.toString());
            connectionRegistry.remove(getUserName());
            logger.log(Level.SEVERE, Config.LogMessage.SERVER_CONNECTION_TERMINATED_ERROR.toString(), new String[]{getUserName(), e.getMessage()});
        } catch (IOException e) {
            logger.log(Level.SEVERE, Config.LogMessage.HANDLER_COMMUNICATION_ERROR.toString(), e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, Config.LogMessage.SERVER_UNKNOWN_TYPE_ERROR.toString(), e.getMessage());
        }
        logger.log(Level.INFO, Config.LogMessage.SERVER_CONNECTION_STOP_MESSAGE.toString(), getUserName());
    }

    /**
     * Closes the actual connection and handles possible exceptions
     */
    @Override
    public void stopReceiving() {
        logger.log(Level.INFO, Config.LogMessage.SERVER_HANDLER_CLOSE_MESSAGE.toString(), getUserName());
        try {
            logger.log(Level.INFO, Config.LogMessage.HANDLER_RECEIVING_STOP_MESSAGE.toString());
            closeConnection();
            logger.log(Level.INFO, Config.LogMessage.HANDLER_RECEIVING_STOPED_MESSAGE.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, Config.LogMessage.HANDLER_CONNECTION_CLOSE_ERROR.toString(), e);
        }
        logger.log(Level.INFO, Config.LogMessage.SERVER_CONNECTION_CLOSED_MESSAGE.toString(), getUserName());
    }

    /**
     * Handles the datatype CONFIRM as input
     * The server is not expecting a CONFIRM message, so this is just an error message
     *
     * @param networkMessage network message
     */
    @Override
    protected void handleConfirmType(NetworkMessage networkMessage) {
        logger.log(Level.WARNING, Config.LogMessage.SERVER_NO_CONFIRM_MESSAGE.toString());
    }

    /**
     * Handles the datatype CONNECT as input
     *
     * @param networkMessage network message
     */
    @Override
    protected void handleConnectType(NetworkMessage networkMessage) {
        String sender = networkMessage.getSender();

        if (isStateValid(networkMessage.getType(), Config.State.NEW)) {
            if (connectionRegistry.containsKey(sender)) {
                logger.log(Level.WARNING, Config.LogMessage.SERVER_USERNAME_TAKEN_MESSAGE.toString(), sender);
            } else {
                if (sender == null || sender.isBlank()) {
                    setAnonymousUserName();
                } else {
                    setUserName(sender);
                }
                logger.log(Level.INFO, Config.LogMessage.SERVER_USERNAME_SET_MESSAGE.toString(), new String[]{getConnection().getRemoteHost(), Integer.toString(getConnection().getRemotePort()), getUserName()});
                connectionRegistry.put(getUserName(), this);
                sendNetworkMessage(
                    new NetworkMessage(Config.UserMask.NONE.toString(),
                        getUserName(),
                        NetworkMessage.DataType.CONFIRM,
                        String.format(Config.UIMessage.SERVER_REGISTRATION_SUCCESSFUL_MESSAGE.toString(), getUserName()))
                );
                setState(Config.State.CONNECTED);
            }
        }
    }

    /**
     * Handles the datatype DISCONNECT as input
     *
     * @param networkMessage network message
     */
    @Override
    protected void handleDisconnectType(NetworkMessage networkMessage) {
        if (isStateValid(networkMessage.getType(), Config.State.CONNECTED)) {
            connectionRegistry.remove(getUserName());
            sendNetworkMessage(
                new NetworkMessage(Config.UserMask.NONE.toString(),
                    getUserName(),
                    NetworkMessage.DataType.CONFIRM,
                    String.format(Config.UIMessage.SEVER_CONFIRM_DISCONNECT_MESSAGE.toString(), getUserName()))
            );
        }
        setState(Config.State.DISCONNECTED);
        this.stopReceiving();
    }

    /**
     * Handles the datatype MESSAGE as input
     *
     * @param networkMessage network message
     */
    @Override
    protected void handleMessageType(NetworkMessage networkMessage) {
        String receiver = networkMessage.getReceiver();

        if (isStateValid(networkMessage.getType(), Config.State.CONNECTED)) {
            if (Config.UserMask.ALL.equals(receiver)) {
                for (ServerConnectionHandler handler : connectionRegistry.values()) {
                    handler.sendNetworkMessage(networkMessage);
                }
            } else {
                ServerConnectionHandler handlerReceiver = connectionRegistry.get(receiver);
                ServerConnectionHandler handlerSender = connectionRegistry.get(networkMessage.getSender());
                if (handlerReceiver != null) {
                    handlerReceiver.sendNetworkMessage(networkMessage);
                    handlerSender.sendNetworkMessage(networkMessage);
                } else {
                    sendNetworkMessage(
                        new NetworkMessage(Config.UserMask.NONE.toString(),
                            getUserName(),
                            NetworkMessage.DataType.ERROR,
                            String.format(Config.UIMessage.SERVER_UNKNOWN_USER_ERROR.toString(), receiver))
                    );
                }
            }
        }
    }

    /**
     * Handles the datatype ERROR as input
     *
     * @param networkMessage network message
     */
    @Override
    protected void handleErrorType(NetworkMessage networkMessage) {
        logger.log(Level.WARNING, Config.LogMessage.SERVER_RECEIVED_ERROR_MESSAGE.toString(), new String[]{networkMessage.getSender(), networkMessage.getPayload()});
    }

    /**
     * Sets the username to a generated anonymous username
     */
    private void setAnonymousUserName() {
        setUserName(Config.ANONYMOUS_USER_PREFIX + connectionId);
    }

    /**
     * Checks whether a state is what it should be at a datatype and writes an error message to the console
     *
     * @param dataType    datatype
     * @param shouldState should state
     * @return returns whether the state is valid
     */
    private boolean isStateValid(NetworkMessage.DataType dataType, Config.State shouldState) {
        boolean isValid;

        if (getState() == shouldState) {
            isValid = true;
        } else {
            isValid = false;
            logger.log(Level.SEVERE, Config.LogMessage.SERVER_ILLEGAL_STATE_ERROR.toString(), new String[]{dataType.toString(), getState().toString()});
        }
        return isValid;
    }
}
