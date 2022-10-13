package com.example.bankservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankServiceApplication {

    static public String generateBlik(String name, String password){

        return "200";
    }
    public static void main(String[] args) {
        SpringApplication.run(BankServiceApplication.class, args);
    }

}
