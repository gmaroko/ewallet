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
 * @author Doodle
 */
public class EWalletService implements IEWalletService {
    private final Database database;

    public EWalletService(Database db) {
        this.database = db;
    }

    @Override
    public User loginUser(String email, String password) {
        if (email == null || password == null) return null;
        try {
            database.connect();
            boolean valid = database.loginUser(email, password);
            if (!valid) return null;
            return database.getUserByEmail(email);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            try { database.disconnect(); } catch (SQLException e) {}
        }
    }

    @Override
    public boolean registerUser(String name, String email, String password) {
        if (name == null || email == null || password == null) return false;
        // ref: https://support.boldsign.com/kb/article/15962/how-to-create-regular-expressions-regex-for-email-address-validation
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) return false;
        
        try {
            database.connect();
            
            // TODO - add check to see if user already exists
            User user = new User(
                    CommonUtils.userIdGenerator(),
                    name,
                    email,
                    password,
                    "USER",
                    0.0
            );
            return database.registerUser(user);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try { database.disconnect(); } catch (SQLException e) {}
        }
    }

    @Override
    public boolean depositFunds(String userId, double amount) {
        if (amount <= 0 || amount < CommonUtils.MINIMUM_DEPOSIT_AMOUNT) return false;
        try {
            database.connect();
            double currentBalance = database.getBalance(userId);
            boolean updated = database.updateBalance(userId, currentBalance + amount);

            if (updated) {
                Transaction tx = new Transaction("DEPOSIT", amount, 0.0, userId, userId);
                database.recordTransaction(tx);
            }
            return updated;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try { database.disconnect(); } catch (SQLException e) {}
        }
    }

    @Override
    public boolean withdrawFunds(String userId, double amount) {
        if (amount <= 0) return false;
        try {
            database.connect();
            double currentBalance = database.getBalance(userId);
            if (currentBalance < amount) return false;
            return database.updateBalance(userId, currentBalance - amount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try { database.disconnect(); } catch (SQLException e) {}
        }
    }
    
    /**
     * interesting to think of how the transfer funds should work and what is a failure happens: commit and Rollback
     * - Ref: https://www.geeksforgeeks.org/java/transactions-in-jdbc/
     * - Ref: https://www.geeksforgeeks.org/dbms/concurrency-problems-in-dbms-transactions/
     */
    @Override
    public boolean transferFunds(String senderId, String receiverId, double amount) {
        if (amount <= 0 || amount < CommonUtils.MINIMUM_SEND_AMOUNT) return false;
        try {
            database.connect();
            double senderBalance = database.getBalance(senderId);
            double fee = CommonUtils.calculateTransactionFee(amount);
            double total = amount + fee;

            if (senderBalance < total) return false;

            database.conn.setAutoCommit(false);
            boolean dr = database.updateBalance(senderId, senderBalance - total);
            double receiverBalance = database.getBalance(receiverId);
            boolean cr = database.updateBalance(receiverId, receiverBalance + amount);

            if (dr && cr) {
                Transaction tx = new Transaction("SEND", amount, fee, senderId, receiverId);
                Transaction rx = new Transaction("RECEIVED", amount, fee, senderId, receiverId);
                database.recordTransaction(tx);
                database.recordTransaction(rx);
                database.conn.commit();
                return true;
            } else {
                database.conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try { database.disconnect(); } catch (SQLException e) {}
        }
    }

    @Override
    public List<Transaction> getTransactionsForUser(String userId) {
        try {
            database.connect();
            return database.getTransactionsForUser(userId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        } finally {
            try { database.disconnect(); } catch (SQLException e) {}
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        try {
            database.connect();
            return database.getAllTransactions();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        } finally {
            try { database.disconnect(); } catch (SQLException e) {}
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            database.connect();
            return database.getAllUsers();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        } finally {
            try { database.disconnect(); } catch (SQLException e) {}
        }
    }

    @Override
    public boolean resetPassword(String email, String password) {
        try {
            database.connect();
            
            // TODO - add check to see if user already exists
            User user = database.getUserByEmail(email);
            if(user != null){
                return database.updatePassword(user, password);
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try { database.disconnect(); } catch (SQLException e) {}
        }
    }
}