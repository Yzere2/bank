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
    String GuidToken;
    int IsReceived;
    float Amount;
    String BlikToken;

}
