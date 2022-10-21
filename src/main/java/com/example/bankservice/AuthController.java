package com.example.bankservice;

import com.example.bankservice.repository.TokenRepository;
import com.example.bankservice.repository.TransactionRepository;
import com.example.bankservice.repository.UserRepository;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://terminal.io")
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

    @CrossOrigin
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public int code5(){
        return 200;
    }
    @RequestMapping(name = "/fetch", method = RequestMethod.POST)
    public int createFetch() throws IOException {
        return 210;
    }
    @CrossOrigin
    @RequestMapping(value = "/user/new", produces = "application/json")
    public String postBodyNewUser(@RequestBody Map<String, Object> postData) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
        //Enter User data from post
        UserRepository userRepository = new UserRepository();

        User user = new User();

        user.Name = (String) postData.get("Name");
        System.out.println(user.Name);
        List<String> names = postBodyUsersName();
        System.out.println(names);
        System.out.println(names.contains(user.Name.toLowerCase()));
        if(names.contains(user.Name.toLowerCase())){
            return "ta nazwa uzytkownika jest zajeta";
        }
        List<String> guids = postBodyUsersGuid();
        user.GuidToken = java.util.UUID.randomUUID().toString();
        while(guids.contains(user.GuidToken)){
            user.GuidToken = java.util.UUID.randomUUID().toString();
        }

        user.PrincipalName = user.Name+"@company.com";
        int execute = Integer.parseInt(String.valueOf(postData.get("Execute")));


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
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO `Users` (`id`, `Name`, `PrincipalName`, `PasswordHash`, `GuidToken`, `Salt`, `AuthHash`) " +
                    "VALUES (NULL, ?, ?, ?, ?, ?, ?)");

            //Assigning values to statement parameters
            stmt.setString(1, user.Name);
            stmt.setString(2, user.PrincipalName);
            stmt.setString(3, user.PasswordHash);
            stmt.setString(4, user.GuidToken);
            stmt.setString(5, user.Salt);
            stmt.setString(6, NewHash);

            //Execute the statement and add the user
            stmt.executeUpdate();

            //Close connection for God's sake!!!!
            connection.close();
        }
        //Assures hash Gen went good :)
        return NewerHash[6];
    }


    @CrossOrigin
    @RequestMapping(value = "/user/auth", produces = "application/json")
    public String postBodyAuthUser(@RequestBody Map<String, Object> postData) throws SQLException {
        int execute = Integer.valueOf(postData.get("Execute").toString());

        String name = (String) postData.get("Name");
        String password = (String) postData.get("Password");

        User user = userRepository.getNameUser(name);
        String hash = user.getAuthHash();

        Argon2 argon2 = Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2id,
                16,
                32);
        System.out.println(argon2.verify(hash, password));
        String blikCode = null;
        if (argon2.verify(hash, password)) {
            blikCode = Token.blikCodeGen(user.getGuidToken());
            System.out.println(blikCode);
            tokenRepository.createToken(1, blikCode, user.getGuidToken());
            return blikCode;
        }else{return "zle dane logowania";}
    }

    @CrossOrigin
    @RequestMapping(value = "/user/listdepr", produces = "application/json")
    public String postBodyListUsersDepreciated(@RequestBody Map<String, Object> postData) throws SQLException {


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


    @CrossOrigin
    @RequestMapping(value = "/user/list", produces = "application/json")
    public Object postBodyUsers(@RequestBody Map<String, Object> postData){
        Object allUsers = userRepository.getAllUsers();
        return allUsers;
    }
    @RequestMapping(value = "/user/list/guid", produces = "application/json")
    public List<String> postBodyUsersGuid(){
        List<User> guidUser = userRepository.getAllUsersGuid();
        List<String> guids = new ArrayList<>();
        for (int i=0; i<guidUser.size(); i++) {
            System.out.println(guidUser.get(i).GuidToken);
            guids.add(guidUser.get(i).GuidToken);
        }
        System.out.println(guids);
        return guids;
    }

    @RequestMapping(value = "/user/list/name", produces = "application/json")
    public List<String> postBodyUsersName(){
        List<User> nameUser = userRepository.getAllUsersName();
        List<String> names = new ArrayList<>();
        for (int i=0; i<nameUser.size(); i++) {
            System.out.println(nameUser.get(i).Name);
            names.add(nameUser.get(i).Name);
        }
        System.out.println(names);
        return names;
    }

    @CrossOrigin
    @RequestMapping(value = "/user/list/id", produces = "application/json")
    public Object postBodyUsersId(@RequestBody Map<String, Object> postData){
        Object allIdUsers = userRepository.getIdUser((Integer) postData.get("Id"));
        return allIdUsers;
    }
    @RequestMapping(value = "/token/list", produces = "application/json")
    public Object postBodyTokens(@RequestBody Map<String, Object> postData){
        Object allTokens = tokenRepository.getAllTokens();
        return allTokens;
    }

//    @RequestMapping(value = "/token/id", produces = "application/json")
//    public Object postBodyIdTokens(@RequestBody Map<String, Object> postData){
//        Object token = tokenRepository.getCodeToken("879560");
//        return token;
//    }
    @RequestMapping(value = "/transaction/list/", produces = "application/json")
    public Object postBodyTransactions(@RequestBody Map<String, Object> postData){
        Object allTransactions = transactionRepository.getAllTransactions();
        return allTransactions;
    }

    @RequestMapping(value = "/transaction/auth", produces = "application/json")
    public String postBodyTransactionAuth(@RequestBody Map<String, Object> postData) throws SQLException {

        int execute = Integer.parseInt(postData.get("Execute").toString());
        int amount = Integer.parseInt(postData.get("Ammount").toString());
        String blikCode = postData.get("BlikCode").toString();


        String name = (String) postData.get("Name");
        String password = (String) postData.get("Password");

        User user = userRepository.getNameUser(name);
        String hash = user.getAuthHash();

        Argon2 argon2 = Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2id,
                16,
                32);
        System.out.println(argon2.verify(hash, password));

        if (argon2.verify(hash, password)) {
            Transaction send = new Transaction();
            Transaction receive = new Transaction();

            receive.setGuidToken(user.getGuidToken());
            send.setGuidToken(tokenRepository.getCodeToken(blikCode).GuidToken);

            receive.setIsReceived(1);
            send.setIsReceived(0);

            receive.setAmount(amount);
            send.setAmount(-amount);

            receive.setBlikToken(tokenRepository.getCodeToken(blikCode).BlikToken);
            send.setBlikToken(tokenRepository.getCodeToken(blikCode).BlikToken);

            transactionRepository.createTransaction(Integer.parseInt(postData.get("Execute").toString()), receive.GuidToken, receive.IsReceived, receive.Amount, receive.BlikToken);
            transactionRepository.createTransaction(Integer.parseInt(postData.get("Execute").toString()), send.GuidToken, send.IsReceived, send.Amount, send.BlikToken);

            return "udalo sie";
        }else{return "zle dane logowania";}
    }











    //SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
    //Date date = df.parse("2019-08-07T14:00:00-0400");

}
