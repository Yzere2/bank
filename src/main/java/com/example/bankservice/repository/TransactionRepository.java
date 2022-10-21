package com.example.bankservice.repository;


import com.example.bankservice.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Repository
public class TransactionRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<Transaction> getAllTransactions(){
        return jdbcTemplate.query("SELECT * FROM Transactions", BeanPropertyRowMapper.newInstance(Transaction.class));
    }

    public void createTransaction(int execute, String guidToken, int isReceived, BigDecimal amount, String blikToken) throws SQLException {

        if(execute==1){
            //Connecting to Database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/bank",
                    "root", ""
            );

            //Preparing Statement
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO `Transactions` (`Id`,`GuidToken`, `IsReceived`, `Amount`, `BlikToken`) " +
                            "VALUES (NULL, ?, ?, ?, ?)");
            //Assigning values to statement parameters
            stmt.setString(1, guidToken);
            stmt.setString(2, String.valueOf(isReceived));
            if(isReceived==0){stmt.setBigDecimal(3, amount.negate());}
            if(isReceived==1){stmt.setBigDecimal(3, amount);}
            stmt.setString(4, blikToken);
            //Execute the statement and add the user
            stmt.executeUpdate();

            //Close connection for God's sake!!!!
            connection.close();
        }
    }

}
