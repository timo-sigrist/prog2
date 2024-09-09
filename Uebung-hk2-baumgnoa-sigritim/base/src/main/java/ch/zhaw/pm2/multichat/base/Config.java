package ch.zhaw.pm2.multichat.base;

import java.util.regex.Pattern;

/**
 * This Class contains all necessary assets for the multichat-project.
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public class Config {
    // validation-regex
    public static final Pattern MESSAGE_PATTERN = Pattern.compile("^(?:@(\\w*[-]?[0-9]*))?\\s*(.*)$");
    public static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    public static final Pattern HOSTNAME_PATTERN = Pattern.compile("^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$");
    public static final Pattern PORT_PATTERN = Pattern.compile("^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");

    public static final String ANONYMOUS_USER_PREFIX = "Anonymous-";
    public static final String CLIENT_UI_TITLE = "Multichat Client";

    // logger
    public static final String CLIENT_LOGGER = "ClientLogger";
    public static final String SERVER_LOGGER = "ServerLogger";

    /**
     * This enum contains all messages displayed in the front-end
     */
    public enum UIMessage {
        //Message-formats
        ERROR_FORMAT("[ERROR] %s\n"),
        MESSAGE_FORMAT("[%s -> %s] %s\n"),
        INFO_FORMAT("[INFO] %s\n"),

        // Connection-status
        CONNECT("Connect"),
        DISCONNECT("Disconnect"),

        //Info-messages
        SERVER_CLOSED_MESSAGE_TO_CLIENTS("Server closed. Forced disconnect of %s"),
        SERVER_REGISTRATION_SUCCESSFUL_MESSAGE("Registration successfull for %s"),
        SEVER_CONFIRM_DISCONNECT_MESSAGE("Confirm disconnect of %s"),

        //Error-messages
        CLIENT_UNEXPECTED_TYPE_ERROR("Unexpected message type: "),
        CLIENT_IP_HOSTNAME_ERROR("Illegal ip-address or port %s"),
        CLIENT_PORT_ERROR("Illegal port %s"),
        NO_HANDLER_ERROR("No connection handler"),
        MESSAGE_ERROR("Not a valid message format."),
        SERVER_UNKNOWN_USER_ERROR("Unknown User: %s");

        private final String message;

        UIMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    /**
     * This enum contains all messages displayed in the log
     */
    public enum LogMessage {
        // Client-messages
        CLIENT_START_MESSAGE("Starting Client Application"),
        CLIENT_END_MESSAGE("Client Application ended"),

        //Client-error
        CLIENT_UI_START_ERROR("Error starting up UI {0}"),

        // Server-messages
        SERVER_CONNECTED_MESSAGE("Connected new Client with IP:Port <{0}:{1}>"),
        SERVER_CONNECTION_TERMINATED_MESSAGE("Server connection terminated"),
        SERVER_STARTED_MESSAGE("Server started."),
        SERVER_CREATE_CONNECTION_MESSAGE("Create server connection"),
        SERVER_CREATED_MESSAGE("Listening on {0}:{1}"),
        SERVER_STOPPED_MESSAGE("Server Stopped."),
        SERVER_START_CLOSE_MESSAGE("Close server port."),
        SERVER_CLOSED_ERROR_MESSAGE("Failed to close server connection: {0}"),
        SERVER_COMMUNICATION_ERROR("Communication error {0}"),

        // ServerManager-messages
        SERVER_ILLEGAL_ARGUMENTS_MESSAGE("Illegal number of arguments:  [<ServerPort>]"),
        SERVER_SHUTDOWN_START_MESSAGE("Shutdown initiated..."),
        SERVER_SHUTDOWN_COMPLETE_MESSAGE("Shutdown complete."),
        SERVER_START_ERRORMESSAGE("Error while starting server. {0}"),

        // ServerManager-error
        SERVER_SHUTDOWN_INTERRUPTED_ERROR("Warning: Shutdown interrupted. {0}"),

        // ConnectionHandler-messages
        HANDLER_RECEIVING_START_MESSAGE("Start receiving data..."),
        HANDLER_RECEIVING_STOP_MESSAGE("Stop receiving data..."),
        HANDLER_RECEIVING_STOPED_MESSAGE("Stopped receiving data"),

        // ConnectionHandler-errors,
        HANDLER_UNKNOWN_DATATYPE_ERROR("Unknown data type received: {0}"),
        HANDLER_CONNECTION_CLOSED_ERROR("Connection closed: {0}"),
        HANDLER_CONNECTION_TERMINATED_ERROR("Connection terminated by remote"),
        HANDLER_CONNECTION_CLOSE_ERROR("Failed to close connection. {0}"),
        HANDLER_COMMUNICATION_ERROR("Communication error: {0}"),
        HANDLER_PROCESS_ERROR("Error while processing data {0}"),
        HANDLER_NO_SENDER_FOUND_ERROR("No Sender found"),
        HANDLER_NO_RECEIVER_FOUND_ERROR("No Receiver found"),
        HANDLER_NO_TYPE_FOUND_ERROR("No Type found"),

        //Client-ConnectionHandler-messages,
        CLIENT_HANDLER_START_MESSAGE("Starting Client-Connection Handler"),
        CLIENT_CONNECTION_STOP_MESSAGE("Stopped Connection Handler"),
        CLIENT_HANDLER_CLOSE_MESSAGE("Closing Connection Handler to Server"),
        CLIENT_CONNECTION_CLOSE_MESSAGE("Closed Connection Handler to Server"),
        CLIENT_CONFIRM_PAYLOAD_MESSAGE("CONFIRM: {0}"),
        CLIENT_DISCONNECT_PAYLOAD_MESSAGE("DISCONNECT: {0}"),
        CLIENT_ALREADY_DISCONNECTED_MESSAGE("DISCONNECT: Already in disconnected: {0}"),
        CLIENT_ILLEGAL_CONNECT_REQUEST_MESSAGE("Illegal connect request from server"),
        CLIENT_ILLEGAL_STATE_MESSAGE("MESSAGE: Illegal state {0} for message: {1}"),
        CLIENT_SENT_MESSAGE("MESSAGE: From {0} to {1}: {2}"),

        //Client-ConnectionHandler-errors,
        HANDLER_CONNECTION_TERMINATED_LOCAL_ERROR("Connection terminated locally"),
        CLIENT_CONNECTION_UNREGISTERED_ERROR("Unregistered because connection terminated {0}"),
        CLIENT_COMMUNICATION_ERROR("Communication error {0}"),
        CLIENT_UNKNOWN_TYPE_ERROR("Received object of unknown type"),
        CLIENT_CONNECT_STATE_ERROR("Illegal state for connect: %s"),
        CLIENT_DISCONNECT_STATE_ERROR("Illegal state for disconnect: %s"),
        CLIENT_MESSAGE_STATE_ERROR("Illegal state for message: %s"),
        CLIENT_UNEXPECTED_PAYLOAD_ERROR("Got unexpected confirm message: {0}"),
        CLIENT_ERROR_PAYLOAD_ERROR("ERROR: {0}"),

        // Server-ConnectionHandler-messages
        SERVER_HANDLER_START_MESSAGE("Starting Server-Connection Handler"),
        SERVER_CONNECTION_STOP_MESSAGE("Stopping Connection Handler for {0}"),
        SERVER_HANDLER_CLOSE_MESSAGE("Closing Connection Handler for {0}"),
        SERVER_CONNECTION_CLOSED_MESSAGE("Closed Connection Handler for {0}"),
        SERVER_NO_CONFIRM_MESSAGE("Not expecting to receive a CONFIRM request from client"),
        SERVER_USERNAME_TAKEN_MESSAGE("User name already taken: {0}"),
        SERVER_RECEIVED_ERROR_MESSAGE("Received error from client ({0}): {1}"),
        SERVER_USERNAME_SET_MESSAGE("Username for client <{0}:{1}> was set to: {2}"),

        //Server-ConnectionHandler-errors
        SERVER_CONNECTION_TERMINATED_ERROR("Unregistered because client connection terminated: {0} {1}"),
        SERVER_UNKNOWN_TYPE_ERROR("Received object of unknown type: {0}"),
        SERVER_ILLEGAL_STATE_ERROR("Illegal state for {0} request: {1}");

        private final String message;

        LogMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    /**
     * UserMask defines standard masks for the user selection
     */
    public enum UserMask {
        NONE(""),
        ALL("*");

        private final String mask;

        UserMask(String mask) {
            this.mask = mask;
        }

        @Override
        public String toString() {
            return mask;
        }

        public boolean equals(String mask) {
            return this.mask.equals(mask);
        }
    }

    /**
     * State defines the different states of connections
     */
    public enum State {
        NEW, CONFIRM_CONNECT, CONNECTED, CONFIRM_DISCONNECT, DISCONNECTED
    }

}
