package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    public static Connection connection(String DBname, String name, String passwd){
        Connection con = null;
        String uri = "jdbc:msyql://localhost:3306" + DBname + "?useSSL=true&characterEncoding=utf-8";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection(uri, name, passwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }


}
