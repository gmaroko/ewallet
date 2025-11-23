/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.groupproject.ewallet;

import java.util.List;

/**
 *
 * @author gmaroko
 */

public interface IEWalletService {
    User loginUser(String email, String password);
    boolean registerUser(String name, String email, String password);
    boolean resetPassword(String userId, String password);
    boolean depositFunds(String userId, double amount);
    boolean withdrawFunds(String userId, double amount);
    boolean transferFunds(String senderId, String receiverId, double amount);
    List<Transaction> getTransactionsForUser(String userId);
    List<Transaction> getAllTransactions();
    List<User> getAllUsers();
}
