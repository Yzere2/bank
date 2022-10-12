package com.example.bankservice;

import java.sql.*;

public class Mapping {

    public static ResultSet getUsers(Object id) throws SQLException {

        //Connecting to Database
        Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/bank",
                "root", ""
        );

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Users");

        //Close connection for God's sake!!!!
        connection.close();

        return rs;
    }

}
