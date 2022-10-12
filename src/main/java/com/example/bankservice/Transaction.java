package com.example.bankservice;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {

    int id;
    int UserId;
    boolean IsReceived;
    double Amount;
    Instant instant = Instant.now();

}
