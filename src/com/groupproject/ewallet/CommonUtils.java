/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.groupproject.ewallet;

import java.util.Random;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;


/**
 *
 * @author gmaroko
 */
public class CommonUtils {
    
    public static final double MINIMUM_DEPOSIT_AMOUNT = 100.0;
    public static final double MINIMUM_WITHDRAW_AMOUNT = 100.0;
    public static final double MINIMUM_SEND_AMOUNT = 100.0;
    
    public static double calculateTransactionFee(double amount){
        /**
         * We want to assume some random values, but we can update it later accordingly.
         * 0-100 - 0%
         * 101 - 500 - 1%
         * 500 - 1000 - 2%
         * 1000 - 2000 - 3% 
         * 2001 - 5000 - 4%
         * 5001+ - 5%
        */
        
        double fee = 0.0;
        
        if (amount > 101 && amount <= 500){
            fee = amount * 0.01;
        } else if (amount > 500 && amount <= 1000){
            fee = amount * 0.02;
        } else if (amount > 1000 && amount <= 2000){
            fee = amount * 0.03;
        } else if (amount > 2000 && amount <= 5000){
            fee = amount * 0.04;
        } else if (amount > 5000){
            fee = amount * 0.05;
        }
        return fee;
    } 
    
    
    public static String userIdGenerator(){
        Random random = new Random();
        int id = 100000 + random.nextInt(999999);
        return String.valueOf(id);
    }
    
    public static String transactionIdGenerator(){
        return "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
    
    
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}
