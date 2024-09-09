package ch.zhaw.pm2.multichat.client;

import ch.zhaw.pm2.multichat.base.ChatProtocolException;
import ch.zhaw.pm2.multichat.base.Config;
import ch.zhaw.pm2.multichat.base.NetworkHandler;
import ch.zhaw.pm2.multichat.base.NetworkMessage;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;


/**
 * Represents the controller for the chat window and with that the controller of MVC
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public class ChatWindowController {
    private ClientConnectionHandler connectionHandler;
    private ClientMessageModel clientMessageModel;
    private ExecutorService executorService;
    private final WindowCloseHandler windowCloseHandler = new WindowCloseHandler();

    @FXML
    private Pane rootPane;
    @FXML
    private TextField serverAddressField;
    @FXML
    private TextField serverPortField;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField messageField;
    @FXML
    private TextArea messageArea;
    @FXML
    private Button connectButton;
    @FXML
    private Button sendButton;
    @FXML
    private TextField filterValue;

    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        serverAddressField.setText(NetworkHandler.DEFAULT_ADDRESS.getCanonicalHostName());
        serverPortField.setText(String.valueOf(NetworkHandler.DEFAULT_PORT));
        handleStateChanged(Config.State.NEW);
        clientMessageModel = new ClientMessageModel();
        messageArea.textProperty().bind(clientMessageModel.getMessageProperty());
    }

    /**
     * Closes the application
     */
    private void closeApplication() {
        connectionHandler.setState(Config.State.DISCONNECTED);
    }

    /**
     * Toggles the connection. If it's not connected it connects and if it's connected it disconnects
     */
    @FXML
    private void toggleConnection() {
        if (connectionHandler == null || connectionHandler.getState() != Config.State.CONNECTED) {
            connect();
        } else {
            disconnect();
        }
    }

    /**
     * Connects the client to the server
     */
    private void connect() {
        try {
            clientMessageModel.clearMessages();
            if (startConnectionHandler()) {
                connectionHandler.connect();
            }
        } catch (ChatProtocolException | IOException e) {
            addError(e.getMessage());
        }
    }

    /**
     * Disconnects the client from the server
     */
    private void disconnect() {
        if (connectionHandler == null) {
            addError(Config.UIMessage.NO_HANDLER_ERROR.toString());
        } else {
            try {
                connectionHandler.disconnect();
            } catch (ChatProtocolException e) {
                addError(e.getMessage());
            }
        }
    }

    /**
     * Sends a message
     */
    @FXML
    private void sendMessage() {
        if (connectionHandler == null) {
            addError(Config.UIMessage.NO_HANDLER_ERROR.toString());
        } else {
            String messageString = messageField.getText().strip();
            Matcher matcher = Config.MESSAGE_PATTERN.matcher(messageString);
            if (matcher.find()) {
                String receiver = matcher.group(1);
                String message = matcher.group(2);
                if (receiver == null || receiver.isBlank()) receiver = Config.UserMask.ALL.toString();
                try {
                    connectionHandler.sendMessage(receiver, message);
                } catch (ChatProtocolException e) {
                    addError(e.getMessage());
                }
            } else {
                addError(Config.UIMessage.MESSAGE_ERROR.toString());
            }
        }
    }

    /**
     * Applies a given filter
     */
    @FXML
    private void applyFilter() {
        this.redrawMessageList();
    }

    /**
     * Starts the connection handler to receive and send data
     *
     * @throws IOException input output exception
     */
    private boolean startConnectionHandler() throws IOException {
        boolean connectionStarted;
        String userName = userNameField.getText();
        String serverAddress = serverAddressField.getText();
        String serverPort = serverPortField.getText();

        Matcher ipMatcher = Config.IP_ADDRESS_PATTERN.matcher(serverAddress);
        Matcher hostnameMatcher = Config.HOSTNAME_PATTERN.matcher(serverAddress);
        Matcher portMatcher = Config.PORT_PATTERN.matcher(serverPort);

        if ((ipMatcher.matches() || hostnameMatcher.matches()) && portMatcher.matches()) {
            executorService = Executors.newSingleThreadExecutor();
            connectionHandler = new ClientConnectionHandler(
                NetworkHandler.openConnection(serverAddress, Integer.parseInt(serverPort)), userName, this);
            executorService.execute(connectionHandler);
            rootPane.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, windowCloseHandler);
            connectionStarted = true;
        } else {
            if (!portMatcher.matches()) {
                addError(String.format(Config.UIMessage.CLIENT_PORT_ERROR.toString(), serverPort));
            } else {
                addError(String.format(Config.UIMessage.CLIENT_IP_HOSTNAME_ERROR.toString(), serverAddress));
            }
            connectionStarted = false;
        }
        return connectionStarted;
    }

    /**
     * Terminates the connection handler instance
     */
    private void terminateConnectionHandler() {
        rootPane.getScene().getWindow().removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, windowCloseHandler);
        if (connectionHandler != null) {
            connectionHandler.stopReceiving();
            connectionHandler = null;
            executorService.shutdown();
        }
    }

    /**
     * Reacts on the change of a state
     *
     * @param newState
     */
    public void handleStateChanged(Config.State newState) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                connectButton.setText((newState == Config.State.CONNECTED || newState == Config.State.CONFIRM_DISCONNECT) ? Config.UIMessage.DISCONNECT.toString() : Config.UIMessage.CONNECT.toString());
            }
        });
        if (newState == Config.State.DISCONNECTED) {
            terminateConnectionHandler();
        }
    }

    /**
     * Sets the username in the ui
     *
     * @param userName username
     */
    void setUserName(String userName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userNameField.setText(userName);
            }
        });
    }

    /**
     * Sets the server address in the ui
     *
     * @param serverAddress server address
     */
    void setServerAddress(String serverAddress) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                serverAddressField.setText(serverAddress);
            }
        });
    }

    /**
     * Sets the server port in the ui
     *
     * @param serverPort
     */
    void setServerPort(int serverPort) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                serverPortField.setText(Integer.toString(serverPort));
            }
        });
    }

    /**
     * Adds a message to the client message model
     *
     * @param networkMessage sent networkMessage
     */
    void addMessage(NetworkMessage networkMessage) {
        clientMessageModel.addMessage(networkMessage.getSender(), networkMessage.getReceiver(), networkMessage.getPayload());
    }

    /**
     * Adds an info to the client message model
     *
     * @param info info messsage
     */
    void addInfo(String info) {
        clientMessageModel.addInfo(info);
    }

    /**
     * Adds an error to the client message model
     *
     * @param error error messsage
     */
    void addError(String error) {
        clientMessageModel.addError(error);
    }

    /**
     * Redraws the message list in the ui
     */
    private void redrawMessageList() {
        Platform.runLater(() -> clientMessageModel.writeFilteredMessages(filterValue.getText().strip()));
    }

    /**
     * Local window close handler to close the connection on closing the client app
     */
    class WindowCloseHandler implements EventHandler<WindowEvent> {
        /**
         * Handle the close action
         *
         * @param event event
         */
        @Override
        public void handle(WindowEvent event) {
            closeApplication();
        }
    }
}
