package com.example.bankservice.repository;


import com.example.bankservice.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<Transaction> getAllTransactions(){
        return jdbcTemplate.query("SELECT * FROM Transactions", BeanPropertyRowMapper.newInstance(Transaction.class));
    }


}
