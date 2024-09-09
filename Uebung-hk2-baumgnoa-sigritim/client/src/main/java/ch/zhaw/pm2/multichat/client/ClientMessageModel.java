package ch.zhaw.pm2.multichat.client;

import ch.zhaw.pm2.multichat.base.Config;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the message model and with that the model of MVC
 *
 * @author baumgnoa, sigritim
 * @version 14.04.2022
 */
public class ClientMessageModel {
    private final List<MessageType> typeList = new ArrayList<>();
    private final List<String> senderList = new ArrayList<>();
    private final List<String> receiverList = new ArrayList<>();
    private final List<String> messageList = new ArrayList<>();
    private final StringProperty messages = new SimpleStringProperty();

    /**
     * Updates the message property
     *
     * @param updateValue update value
     */
    private void updateMessageProperty(String updateValue) {
        messages.set(messages.get() + updateValue);
    }

    /**
     * Adds a message to the list and with that also the receiver and the sender
     *
     * @param sender   sender
     * @param receiver receiver
     * @param message  message
     */
    void addMessage(String sender, String receiver, String message) {
        typeList.add(MessageType.MESSAGE);
        senderList.add(sender);
        receiverList.add(receiver);
        messageList.add(message);
        updateMessageProperty(String.format(Config.UIMessage.MESSAGE_FORMAT.toString(), sender, receiver, message));
    }

    /**
     * Adds an error message to the message list
     *
     * @param error error
     */
    void addError(String error) {
        typeList.add(MessageType.ERROR);
        senderList.add("");
        receiverList.add("");
        messageList.add(error);
        updateMessageProperty(String.format(Config.UIMessage.ERROR_FORMAT.toString(), error));
    }

    /**
     * Adds an info message to the message list
     *
     * @param info
     */
    void addInfo(String info) {
        typeList.add(MessageType.INFO);
        senderList.add("");
        receiverList.add("");
        messageList.add(info);
        updateMessageProperty(String.format(Config.UIMessage.INFO_FORMAT.toString(), info));
    }

    /**
     * Writes the filtered messages
     *
     * @param filter filter
     */
    void writeFilteredMessages(String filter) {
        boolean showAll = filter == null || filter.isBlank();
        clearMessages();
        for (int i = 0; i < senderList.size(); i++) {
            String sender = Objects.requireNonNullElse(senderList.get(i), "");
            String receiver = Objects.requireNonNullElse(receiverList.get(i), "");
            String message = Objects.requireNonNull(messageList.get(i), "");
            if (showAll || sender.contains(filter) || receiver.contains(filter) || message.contains(filter)) {
                switch (typeList.get(i)) {
                    case MESSAGE -> updateMessageProperty(String.format(Config.UIMessage.MESSAGE_FORMAT.toString(), senderList.get(i), receiverList.get(i), messageList.get(i)));
                    case ERROR -> updateMessageProperty(String.format(Config.UIMessage.ERROR_FORMAT.toString(), messageList.get(i)));
                    case INFO -> updateMessageProperty(String.format(Config.UIMessage.INFO_FORMAT.toString(), messageList.get(i)));
                    default -> updateMessageProperty(Config.UIMessage.CLIENT_UNEXPECTED_TYPE_ERROR.toString() + typeList.get(i));
                }
            }
        }
    }

    /**
     * Returns the string property of the messages
     *
     * @return string property of the message list
     */
    StringProperty getMessageProperty() {
        return messages;
    }

    /**
     * Clears the message list
     */
    void clearMessages() {
        messages.set("");
    }

    /**
     * Defines the message types locally
     */
    private enum MessageType {
        INFO, MESSAGE, ERROR;
    }
}
