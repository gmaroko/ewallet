/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.groupproject.ewallet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gmaroko
 */

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/ewallet";
    private static final String USER = "root";
    private static final String PASSWORD = null;

    public Connection conn;

    public void connect() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("DB Open Connection OK");
        }
    }

    public void disconnect() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
            System.out.println("DB Close Connection OK");
        }
    }

    public boolean registerUser(User user) {
        String query = "INSERT INTO users (id, name, email, password, balance, role) VALUES (?, ?, ?, ?, ?, ?)";
        
        String hashedPassword = CommonUtils.hashPassword(user.getPassword());
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, user.getId());
            pst.setString(2, user.getName());
            pst.setString(3, user.getEmail());
            pst.setString(4, hashedPassword);
            pst.setDouble(5, user.getBalance());
            pst.setString(6, user.getRole());
            int success = pst.executeUpdate();
            return  success > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    
    
    public boolean updatePassword(User user, String password) throws SQLException {
        String query = "UPDATE users SET password = ? WHERE id = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, CommonUtils.hashPassword(password));
            pst.setString(2, user.getUserId());
            int success =  pst.executeUpdate();
            return success > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public boolean loginUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            boolean validatePass = rs.next() && CommonUtils.checkPassword(password, rs.getString("password"));
            String msg = validatePass ? "Password check OK" : "Password check FAIL";
            System.out.println(msg);
            return validatePass;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean updateBalance(String userId, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, newBalance);
            pst.setString(2, userId);
            int success = pst.executeUpdate();
            return success > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean recordTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (type, amount, fee, sender_id, receiver_id,id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
    
            pst.setString(1, transaction.getType());
            pst.setDouble(2, transaction.getAmount());
            pst.setDouble(3, transaction.getFee());
            pst.setString(4, transaction.getSenderId());
            pst.setString(5, transaction.getReceiverId());
            pst.setString(6, CommonUtils.transactionIdGenerator());
            int success = pst.executeUpdate();
            return success > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public double getBalance(String userId) {
        String query = "SELECT balance FROM users WHERE id = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0.0;
    }
    
    
    // TODO - this might endup listing all transactions for normal users, perhaps we need to consider
    // when a user1 sends money to user2, we post 2 transactions, one for each.
    public List<Transaction> getTransactionsForUser(String userId) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE sender_id = ? OR (receiver_id = ? AND type = 'RECEIVED') ORDER BY date DESC";
        try{
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, userId);
            pst.setString(2, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Transaction txn = new Transaction(
                    rs.getString("type"),
                    rs.getDouble("amount"),
                    rs.getDouble("fee"),
                    rs.getString("sender_id"),
                    rs.getString("receiver_id")
                );
                transactions.add(txn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transactions;
    }

    
    // for admin user only
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions ORDER BY date DESC";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Transaction txn = new Transaction(
                    rs.getString("type"),
                    rs.getDouble("amount"),
                    rs.getDouble("fee"),
                    rs.getString("sender_id"),
                    rs.getString("receiver_id")
                );
                transactions.add(txn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transactions;
    }
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getDouble("balance")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users ORDER BY name ASC";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery(query);
            while (rs.next()) {
                User user = new User(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    "##############",
                    rs.getString("role"),
                    rs.getDouble("balance")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }
}
