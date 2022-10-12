package com.example.bankservice;

import com.example.bankservice.repository.TokenRepository;
import com.example.bankservice.repository.TransactionRepository;
import com.example.bankservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Map;

@RestController
@RequestMapping(value="/api")
public class AuthController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    TransactionRepository  transactionRepository;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public int code(){
        return 200;
    }
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public int code5(){
        return 205;
    }
    @RequestMapping(name = "/fetch", method = RequestMethod.POST)
    public int createFetch() throws IOException {
        return 210;
    }

    @RequestMapping(value = "/user/new", produces = "application/json")
    public String postBody(@RequestBody Map<String, Object> postData) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
        //Enter User data from post
        User user = new User();
        user.Name = (String) postData.get("Name");
        user.GuidToken = java.util.UUID.randomUUID().toString();
        user.PrincipalName = user.Name+"@company.com";
        int execute = (int) postData.get("Execute");

        //Generating Hash
        String NewHash = user.passwordHashing((String) postData.get("Password"));
        System.out.println(NewHash);

        //Split Argon2 output into values
        String[] NewerHash = NewHash.split("\\$|\\,", 8);

        //Removing empty cell[0]
        for (int n = 1; n<NewerHash.length; n++){
            NewerHash[n-1]=NewerHash[n];
            System.out.println("Newer Hash part: "+NewerHash[n-1]);
        }

        //Entering Salt and Hash
        user.Salt = NewerHash[5];
        user.PasswordHash = NewerHash[6];
        System.out.println(execute);

        //Adding user if set to execute
        if(execute==1){

            //Connecting to Database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/bank",
                    "root", ""
            );

            //Preparing Statement
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO `Users` (`id`, `Name`, `PrincipalName`, `PasswordHash`, `GuidToken`, `Salt`) " +
                    "VALUES (NULL, ?, ?, ?, ?, ?)");

            //Assigning values to statement parameters
            stmt.setString(1, user.Name);
            stmt.setString(2, user.PrincipalName);
            stmt.setString(3, user.PasswordHash);
            stmt.setString(4, user.GuidToken);
            stmt.setString(5, user.Salt);

            //Execute the statement and add the user
            stmt.executeUpdate();

            //Close connection for God's sake!!!!
            connection.close();
        }
        //Assures hash Gen went good :)
        return NewerHash[0];
    }

    @RequestMapping(value = "/user/listdepr", produces = "application/json")
    public String postBodyList(@RequestBody Map<String, Object> postData) throws SQLException {


        ResultSet rs = Mapping.getUsers(postData.get("Id"));
        ResultSetMetaData rsmd = rs.getMetaData();
        System.out.println("querying SELECT * FROM XXX");
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);

                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }


        return 200+"haha";
    }


    @RequestMapping(value = "/user/list", produces = "application/json")
    public Object postBodyUsers(@RequestBody Map<String, Object> postData){
        Object allUsers = userRepository.getAllUsers();
        return allUsers;
    }
    @RequestMapping(value = "/token/list", produces = "application/json")
    public Object postBodyTokens(@RequestBody Map<String, Object> postData){
        Object allTokens = tokenRepository.getAllTokens();
        return allTokens;
    }
    @RequestMapping(value = "/transaction/list", produces = "application/json")
    public Object postBodyTransactions(@RequestBody Map<String, Object> postData){
        Object allTransactions = transactionRepository.getAllTransactions();
        return allTransactions;
    }









    //SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
    //Date date = df.parse("2019-08-07T14:00:00-0400");

}
