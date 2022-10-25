package com.example.bankservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
public class User {
    int id;
    String Name;
    String PrincipalName;
    String PasswordHash;
    String GuidToken;
    String Salt;
    String AuthHash;

    BigDecimal Balance;


    @Autowired
    JdbcTemplate jdbcTemplate;
    public String balanceUpdate(String name, String ammount){
        jdbcTemplate.execute("UPDATE `Users` SET `Balance` = '"+ammount+"' WHERE `Users`.`Name`="+name);
        return "added balance of "+ammount+" to "+name;
    }
    public String passwordHashing(String password) {
            // salt 32 bytes
            // Hash length 64 bytes
            Argon2 argon2 = Argon2Factory.create(
                    Argon2Factory.Argon2Types.ARGON2id,
                    16,
                    32);

            char[] passwordTab = password.toCharArray();
            String hash = argon2.hash(3, // Number of iterations
                    64 * 1024, // 64mb
                    1, // how many parallel threads to use
                    passwordTab);
            System.out.println("Hash + salt of the password: "+hash);
            System.out.println("Password verification success: "+ argon2.verify(hash, password));
        return hash;
    }
}
