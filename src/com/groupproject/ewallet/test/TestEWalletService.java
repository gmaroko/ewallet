package com.groupproject.ewallet.test;

import com.groupproject.ewallet.Database;
import com.groupproject.ewallet.EWalletService;
import com.groupproject.ewallet.IEWalletService;
import com.groupproject.ewallet.Transaction;
import com.groupproject.ewallet.User;
import java.util.List;
/**
 *
 * @author gmaroko
 */

public class TestEWalletService {

    private static IEWalletService service;

    public static void main(String[] args) {
        Database db = new Database();
        service = new EWalletService(db);

        testRegisterUser();
        testLoginUser();
        testDepositFunds();
        testWithdrawFunds();
        testTransferFunds();
        testGetUserByEmail();
        testGetTransactionsForUser();
        testGetAllTransactions();
        testGetAllUsers();
    }

    private static void testRegisterUser() {
        System.out.println("\n=== Test: Register User ===");
        boolean result = service.registerUser("Alice", "alice@example.com", "StrongPass123");
        System.out.println(result ? "Registration successful." : "Registration failed.");
    }

    private static void testLoginUser() {
        System.out.println("\n=== Test: Login User ===");
        User user = service.loginUser("alice@example.com", "StrongPass123");
        if (user != null) {
            System.out.println("Login successful. Welcome, " + user.getName());
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }
    }

    private static void testDepositFunds() {
        System.out.println("\n=== Test: Deposit Funds ===");
        boolean result = service.depositFunds("1", 500.0);
        System.out.println(result ? "Deposit successful." : "Deposit failed.");
    }

    private static void testWithdrawFunds() {
        System.out.println("\n=== Test: Withdraw Funds ===");
        boolean result = service.withdrawFunds("1", 200.0);
        System.out.println(result ? "Withdrawal successful." : "Withdrawal failed (Insufficient funds?).");
    }

    private static void testTransferFunds() {
        System.out.println("\n=== Test: Transfer Funds ===");
        boolean result = service.transferFunds("1", "2", 100.0);
        System.out.println(result ? "Transfer successful." : "Transfer failed (Insufficient funds or invalid IDs?).");
    }

    private static void testGetUserByEmail() {
        System.out.println("\n=== Test: Get User By Email ===");
        User user = service.loginUser("alice@example.com", "StrongPass123");
        if (user != null) {
            System.out.println("User found: " + user.getName() + " | Balance: " + user.getBalance());
        } else {
            System.out.println("User not found.");
        }
    }

    private static void testGetTransactionsForUser() {
        System.out.println("\n=== Test: Get Transactions For User ===");
        List<Transaction> transactions = service.getTransactionsForUser("1");
        if (!transactions.isEmpty()) {
            transactions.forEach(System.out::println);
        } else {
            System.out.println("No transactions found for user.");
        }
    }

    private static void testGetAllTransactions() {
        System.out.println("\n=== Test: Get All Transactions ===");
        List<Transaction> transactions = service.getAllTransactions();
        if (!transactions.isEmpty()) {
            transactions.forEach(System.out::println);
        } else {
            System.out.println("No transactions in the system.");
        }
    }

    private static void testGetAllUsers() {
        System.out.println("\n=== Test: Get All Users ===");
        List<User> users = service.getAllUsers();
        if (!users.isEmpty()) {
            for (User u : users) {
                System.out.println("ID: " + u.getUserId() + " | Name: " + u.getName() +
                        " | Email: " + u.getEmail() + " | Balance: " + u.getBalance());
            }
        } else {
            System.out.println("No users found in the system.");
        }
    }
}