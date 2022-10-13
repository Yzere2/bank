package com.example.bankservice.repository;


import com.example.bankservice.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository{
    @Autowired
        JdbcTemplate jdbcTemplate;
        public List<User> getAllUsers(){
            return jdbcTemplate.query("SELECT * FROM Users", BeanPropertyRowMapper.newInstance(User.class));
        }
        public User getIdUser(int id){
            return jdbcTemplate.queryForObject("SELECT * FROM Users WHERE ID="+id, BeanPropertyRowMapper.newInstance(User.class));
        }
        public User getNameUser(String name){
            String query = "SELECT * FROM Users WHERE Name='"+name+"'";
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(User.class));
        }


    }
