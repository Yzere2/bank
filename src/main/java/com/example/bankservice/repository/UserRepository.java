package com.example.bankservice.repository;


import com.example.bankservice.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class UserRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;

        public List<User> getAllUsers(){
            return jdbcTemplate.query("SELECT * FROM Users", BeanPropertyRowMapper.newInstance(User.class));
        }
        public List<User> getAllUsersGuid(){
            return jdbcTemplate.query("SELECT GuidToken FROM Users", BeanPropertyRowMapper.newInstance(User.class));
        }
        public List<User> getAllUsersName(){
            return jdbcTemplate.query("SELECT Name FROM Users", BeanPropertyRowMapper.newInstance(User.class));
        }
        public User getIdUser(int id){
            return jdbcTemplate.queryForObject("SELECT * FROM Users WHERE ID="+id, BeanPropertyRowMapper.newInstance(User.class));
        }
        public User getGuidUser(String GuidToken){
            return jdbcTemplate.queryForObject("SELECT * FROM Users WHERE GuidToken='"+GuidToken+"'", BeanPropertyRowMapper.newInstance(User.class));
        }
        public User getNameUser(String name){
            String query = "SELECT * FROM Users WHERE Name='"+name+"'";
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(User.class));
        }

        public boolean balanceUpdateManual(String name, BigDecimal balance){
            //jdbcTemplate.execute("UPDATE Users SET Balance='"+balance+"' WHERE Name='"+name+"';");
            jdbcTemplate.update("UPDATE Users SET Balance=? WHERE Name=?",balance.toString(),name);
            return true;
        }
        public boolean balanceCheck(String SenderGuid, String ReciverGuid, BigDecimal amount){
            User sender = getGuidUser(SenderGuid);
            User reciver = getGuidUser(ReciverGuid);
            BigDecimal senderBalance = sender.getBalance();
            if(senderBalance.compareTo(amount)>0){
                System.out.println("senderBalance is higher than amount");
                return true;}
            return false;
        }
        public boolean balanceUpdate(String SenderGuid, String ReciverGuid, BigDecimal amount){
            if(!balanceCheck(SenderGuid, ReciverGuid, amount)){return false;}
            User sender = getGuidUser(SenderGuid);
            BigDecimal senderbalance = sender.getBalance();
            System.out.println(senderbalance);
            User reciver = getGuidUser(ReciverGuid);
            BigDecimal reciverbalance = reciver.getBalance();
            System.out.println(reciverbalance);

            System.out.println(amount+" update");

            BigDecimal senderbalancenew = senderbalance.subtract(amount);
            BigDecimal reciverbalancenew = reciverbalance.add(amount);

            String senderguid = "'"+SenderGuid+"'";
            String reciverguid = "'"+ReciverGuid+"'";


            String sendSQL="UPDATE Users SET Balance="+senderbalancenew+" WHERE Users.GuidToken ="+senderguid;
            String reciveSQL="UPDATE Users SET Balance="+reciverbalancenew+" WHERE Users.GuidToken ="+reciverguid;

            System.out.println(sendSQL);
            System.out.println(reciveSQL);

            jdbcTemplate.execute(sendSQL);
            jdbcTemplate.execute(reciveSQL);
            return true;
        }


    }
