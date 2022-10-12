package com.example.bankservice;

public class Hash {

    public Hash(String type, int v, String params, String salt, String hash){
        this.type=type;
        this.v=v;
        this.params=params;
        this.salt=salt;
        this.hash=hash;
    }

    String type;
    int v;
    String params;
    String salt;
    String hash;
}
