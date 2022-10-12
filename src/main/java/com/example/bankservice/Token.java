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
public class Token {

    int id;
    boolean IsValid;
    String BlikToken;
    String GuidToken;
    String BlikCode;

    Instant instant = Instant.now();

}
