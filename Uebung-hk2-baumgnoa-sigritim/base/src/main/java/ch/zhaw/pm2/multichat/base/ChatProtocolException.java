package ch.zhaw.pm2.multichat.base;

/**
 * Represents an exception in the chat protocol
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public class ChatProtocolException extends Exception {
    /**
     * Exception for a message exception
     *
     * @param message message
     */
    public ChatProtocolException(String message) {
        super(message);
    }
}
