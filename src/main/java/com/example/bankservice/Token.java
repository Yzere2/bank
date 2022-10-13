package com.example.bankservice;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Token {

    int id;
    boolean IsValid;
    String BlikToken;
    String GuidToken;
    String BlikCode;
    Instant instant = Instant.now();

    static public String blikCodeGen(String guidToken){
        Token token = new Token();
        token.GuidToken=guidToken;
        Random random = new Random();
        int upper=899999;
        int int_random = random.nextInt(upper)+100000;

        token.BlikCode=String.valueOf(int_random);
        return token.BlikCode;
    }

}
