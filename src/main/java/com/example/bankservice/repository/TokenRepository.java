package com.example.bankservice.repository;


import com.example.bankservice.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@Repository
public class TokenRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<Token> getAllTokens(){
        return jdbcTemplate.query("SELECT * FROM Tokens", BeanPropertyRowMapper.newInstance(Token.class));
    }

    public void createToken(int execute, String BlikCode, String GuidToken) throws SQLException {
        String BlikToken = Base64.getEncoder().encodeToString(BlikCode.getBytes());
        if(execute==1){

            //Connecting to Database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/bank",
                    "root", ""
            );

            //Preparing Statement
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO `Tokens` (`Id`, `IsValid`, `BLikToken`,`GuidToken`, `BlikCode`) " +
                    "VALUES (NULL, ?, ?, ?, ?)");
            //Assigning values to statement parameters
            stmt.setString(1, "1");
            stmt.setString(2, BlikToken);
            stmt.setString(3, GuidToken);
            stmt.setString(4, BlikCode);

            //Execute the statement and add the user
            stmt.executeUpdate();

            //Close connection for God's sake!!!!
            connection.close();
        }
    }

    public Token getCodeToken(String BlikCode){
        String query = "SELECT * FROM Tokens WHERE BlikCode='"+BlikCode+"'";
        return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Token.class));
    }

}
