package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    // Create a new message
    public Message createMessage(Message message) {

        try {
            if (message == null ||
                message.message_text == null ||
                message.message_text.trim().isEmpty() ||
                message.message_text.length() > 255) {
                return null;
            }

            // Check whether the account exists
            if (accountDAO.getAccountById(message.posted_by) == null) {
                return null;
            }

            return messageDAO.createMessage(message);

        } catch (Exception e) {
            return null;
        }
    }

    // Get all messages
    public List<Message> getAllMessages() {

        try {
            return messageDAO.getAllMessages();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Get message by ID
    public Message getMessageById(int messageId) {

        try {
            return messageDAO.getMessageById(messageId);
        } catch (Exception e) {
            return null;
        }
    }

    // Delete message
    public Message deleteMessage(int messageId) {

        try {
            Message message = messageDAO.getMessageById(messageId);

            if (message == null) {
                return null;
            }

            messageDAO.deleteMessage(messageId);

            return message;

        } catch (Exception e) {
            return null;
        }
    }

    // Update message text
    public Message updateMessage(int messageId, String messageText) {

        try {
            if (messageText == null ||
                messageText.trim().isEmpty() ||
                messageText.length() > 255) {
                return null;
            }

            if (messageDAO.getMessageById(messageId) == null) {
                return null;
            }

            return messageDAO.updateMessage(messageId, messageText);

        } catch (Exception e) {
            return null;
        }
    }

    // Get messages by account ID
    public List<Message> getMessagesByAccountId(int accountId) {

        try {
            return messageDAO.getMessagesByAccountId(accountId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}