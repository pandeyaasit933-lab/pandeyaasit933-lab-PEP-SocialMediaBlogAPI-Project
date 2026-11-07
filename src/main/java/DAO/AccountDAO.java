package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    // Register a new account
    public Account createAccount(Account account) throws SQLException {

        Connection connection = ConnectionUtil.getConnection();

        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

        PreparedStatement ps = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS);

        try {
            ps.setString(1, account.username);
            ps.setString(2, account.password);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            try {
                if (rs.next()) {
                    return new Account(
                            rs.getInt(1),
                            account.username,
                            account.password
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

    // Find account by username
    public Account getAccountByUsername(String username)
            throws SQLException {

        Connection connection = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM account WHERE username = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        try {
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            try {
                if (rs.next()) {
                    return mapAccount(rs);
                }
            } finally {
                rs.close();
            }

            return null;

        } finally {
            ps.close();
        }
    }

    // Verify login credentials
    public Account getAccountByUsernameAndPassword(
            String username, String password) throws SQLException {

        Connection connection = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        try {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            try {
                if (rs.next()) {
                    return mapAccount(rs);
                }
            } finally {
                rs.close();
            }

            return null;

        } finally {
            ps.close();
        }
    }

    // Find account by ID
    public Account getAccountById(int accountId) throws SQLException {

        Connection connection = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM account WHERE account_id = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        try {
            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            try {
                if (rs.next()) {
                    return mapAccount(rs);
                }
            } finally {
                rs.close();
            }

            return null;

        } finally {
            ps.close();
        }
    }

    // Convert database result into Account object
    private Account mapAccount(ResultSet rs) throws SQLException {

        return new Account(
                rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password")
        );
    }
}