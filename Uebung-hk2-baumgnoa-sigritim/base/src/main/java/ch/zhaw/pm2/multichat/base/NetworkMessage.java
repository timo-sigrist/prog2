package ch.zhaw.pm2.multichat.base;

import java.io.Serializable;

/**
 * This class represents s structured-data-object of a network-message which is sent between server and client.
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public class NetworkMessage implements Serializable {
    private final String sender;
    private final String receiver;
    private final DataType type;
    private final String payload;

    /**
     * Defines the datatype of a network message
     */
    public enum DataType {
        CONNECT, CONFIRM, DISCONNECT, MESSAGE, ERROR
    }

    /**
     * Public constructor which requires the following four parameters
     *
     * @param sender   sender
     * @param receiver receiver
     * @param type     datatype
     * @param payload  payload
     */
    public NetworkMessage(String sender, String receiver, DataType type, String payload) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.payload = payload;
    }

    /**
     * Returns the sender
     *
     * @return sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the receiver
     *
     * @return receiver
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Returns the type
     *
     * @return datatype
     */
    public DataType getType() {
        return type;
    }

    /**
     * Returns the payload
     *
     * @return payload
     */
    public String getPayload() {
        return payload;
    }

}
