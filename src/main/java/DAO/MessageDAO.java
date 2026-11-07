package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // Create a new message
    public Message createMessage(Message message) throws SQLException {

        Connection connection = ConnectionUtil.getConnection();

        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

        PreparedStatement ps = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS);

        try {
            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3, message.time_posted_epoch);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            try {
                if (rs.next()) {
                    return new Message(
                            rs.getInt(1),
                            message.posted_by,
                            message.message_text,
                            message.time_posted_epoch
                    );
                }
            } finally {
                rs.close();
            }

            return null;

        } finally {
            ps.close();
        }
    }

    // Retrieve all messages
    public List<Message> getAllMessages() throws SQLException {

        Connection connection = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM message ORDER BY message_id";

        PreparedStatement ps = connection.prepareStatement(sql);
        List<Message> messages = new ArrayList<>();

        try {
            ResultSet rs = ps.executeQuery();

            try {
                while (rs.next()) {
                    messages.add(mapMessage(rs));
                }
            } finally {
                rs.close();
            }

            return messages;

        } finally {
            ps.close();
        }
    }

    // Retrieve message by ID
    public Message getMessageById(int messageId) throws SQLException {

        Connection connection = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM message WHERE message_id = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        try {
            ps.setInt(1, messageId);

            ResultSet rs = ps.executeQuery();

            try {
                if (rs.next()) {
                    return mapMessage(rs);
                }
            } finally {
                rs.close();
            }

            return null;

        } finally {
            ps.close();
        }
    }

    // Delete message by ID
    public boolean deleteMessage(int messageId) throws SQLException {

        Connection connection = ConnectionUtil.getConnection();

        String sql = "DELETE FROM message WHERE message_id = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        try {
            ps.setInt(1, messageId);

            return ps.executeUpdate() > 0;

        } finally {
            ps.close();
        }
    }

    // Update message text
    public Message updateMessage(int messageId, String messageText)
            throws SQLException {

        Connection connection = ConnectionUtil.getConnection();

        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        try {
            ps.setString(1, messageText);
            ps.setInt(2, messageId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return getMessageById(messageId);
            }

            return null;

        } finally {
            ps.close();
        }
    }

    // Retrieve messages by account ID
    public List<Message> getMessagesByAccountId(int accountId)
            throws SQLException {

        Connection connection = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM message WHERE posted_by = ? ORDER BY message_id";

        PreparedStatement ps = connection.prepareStatement(sql);
        List<Message> messages = new ArrayList<>();

        try {
            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            try {
                while (rs.next()) {
                    messages.add(mapMessage(rs));
                }
            } finally {
                rs.close();
            }

            return messages;

        } finally {
            ps.close();
        }
    }

    // Convert database result into Message object
    private Message mapMessage(ResultSet rs) throws SQLException {

        return new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
        );
    }
}