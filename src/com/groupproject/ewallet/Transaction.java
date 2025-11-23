/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.groupproject.ewallet;

/**
 *
 * @author gmaroko
 */

import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private String type; // DEPOSIT, WITHDRAW, TRANSFER
    private double amount;
    private double fee;
    private LocalDateTime date;
    private String senderId;
    private String receiverId;
    
    public Transaction(){
        
    }
    public Transaction(String type, double amount, double fee, String senderId, String receiverId) {
        this.id = CommonUtils.transactionIdGenerator();
        this.type = type;
        this.amount = amount;
        this.fee = fee;
        this.date = LocalDateTime.now();
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    // Getters
    public String getTransactionId() { return this.id; }
    public String getType() { return this.type; }
    public double getAmount() { return this.amount; }
    public double getFee() { return this.fee; }
    public LocalDateTime getDate() { return this.date; }
    public String getSenderId() { return this.senderId; }
    public String getReceiverId() { return this.receiverId; }

    @Override
    public String toString() {
        return "Transaction{" +
                "ID=" + this.id +
                ", Type='" + this.type + '\'' +
                ", Amount=" + this.amount +
                ", Fee=" + this.fee +
                ", Date=" + this.date +
                ", Sender=" + this.senderId +
                ", Receiver=" + this.receiverId +
                '}';
    }
}
