package server;

import java.sql.*;

public class DBManager {
    public static Connection connection(String DBname, String name, String passwd){
        Connection con = null;
        String uri = "jdbc:mysql://localhost:3306/" + DBname + "?useSSL=true&characterEncoding=utf-8";
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

    public static ResultSet executeQuery(Connection con, String sqlStr){
        ResultSet rs = null;
        return rs;
    }

    public static void logout(String user){
        Connection con = DBManager.connection("MyChat", "root", "niuzhuang");
        PreparedStatement preSql;
        String sqlStr = "update user set status='OUTLINE' where name=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, user);
            preSql.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection con = DBManager.connection("MyChat", "root", "niuzhuang");
        try {
            Statement sql = con.createStatement();
            String sqlStr = "select * from user";
            ResultSet rs = sql.executeQuery(sqlStr);
            rs.next();
            String rsName = rs.getString(1);
            String rsPw = rs.getString(2);
            String rsStatus = rs.getString(3);;
            System.out.println(rsName + "\t"+ rsPw +"\t"+rsStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
