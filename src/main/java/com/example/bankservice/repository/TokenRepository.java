package com.example.bankservice.repository;


import com.example.bankservice.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TokenRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<Token> getAllTokens(){
        return jdbcTemplate.query("SELECT * FROM Tokens", BeanPropertyRowMapper.newInstance(Token.class));
    }

}
