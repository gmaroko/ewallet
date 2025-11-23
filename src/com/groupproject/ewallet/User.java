package com.groupproject.ewallet;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gmaroko
 */
public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private double balance;
    private List<Transaction> transactions;
    
    public User(){
        
    }

    public User(String userId, String name, String email, String password, String role, double balance) {
        this.id = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.role = role;
        this.transactions = new ArrayList<>();
    }

    // Getters and Setters
    public String getUserId() { return this.id; }
    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public double getBalance() { return this.balance; }
    public String getPassword() { return this.password; }
    public String getRole() { return this.role; }
    public String getId(){return id;}
    
    public void setBalance(double balance) { this.balance = balance; }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role + ", balance=" + balance + ", transactions=" + transactions + '}';
    }
    
}