package server;

import common.User;
import common.UserStatus;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServices {

    /** 新增用户 */
    public static DBExecuteStatus addUser(User user){
        PreparedStatement preSql;
        Connection con;
        String sqlStr;
        ResultSet rs;
        con = DBManager.connection("MyChat", "root", "niuzhuang");
        if(con == null) return DBExecuteStatus.CONNECTION_ERROR;
        try {
            con.setAutoCommit(false);
            sqlStr = "SELECT * FROM user WHERE name=?";
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, user.getUsername());
            rs = preSql.executeQuery();
            if(!rs.next()){
                sqlStr = "INSERT INTO user VALUES(?, ?, ?)";
                preSql = con.prepareStatement(sqlStr);
                preSql.setString(1, user.getUsername());
                preSql.setString(2, user.getPassword());
                preSql.setString(3, "OUTLINE");
                int ok = preSql.executeUpdate();
                return DBExecuteStatus.OK;
            }else{
                return DBExecuteStatus.EXISTED_ERROR;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                con.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return DBExecuteStatus.ERROR;
    }

    /** user login */
    public static DBExecuteStatus login(String name, String password){
        PreparedStatement preSql;
        Connection con;
        String sqlStr;
        ResultSet rs;
        con = DataBuffer.con;
        if(con == null) return DBExecuteStatus.CONNECTION_ERROR;
        try {
            con.setAutoCommit(false);
            sqlStr = "select * from user where name=? and password=?";
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, name);
            preSql.setString(2, password);
            rs = preSql.executeQuery();
            if(rs.next()){
                String status = rs.getString(3);
                if(UserStatus.valueOf(status) == UserStatus.OUTLINE){
                    sqlStr = "update user set status=? where name=? and password=?";
                    preSql = con.prepareStatement(sqlStr);
                    preSql.setString(1, "LOGGED");
                    preSql.setString(2, name);
                    preSql.setString(3, password);
                    int ok = preSql.executeUpdate();
                    return DBExecuteStatus.OK;
                }else {
                    return DBExecuteStatus.HAS_LOGGED_IN_ERROR;
                }
            }else{
                return DBExecuteStatus.NO_EXIST_ERROR;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                con.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return DBExecuteStatus.ERROR;
    }


    /** 加载所有用户 */
    @SuppressWarnings("unchecked")
    public static List<User> loadAllUser() {
        List<User> list = new ArrayList<User>();
        Connection con = DataBuffer.con;
        PreparedStatement preSql;
        ResultSet rs;
        String sqlStr = "select name,password from user";
        try {
            preSql = con.prepareStatement(sqlStr);
            rs = preSql.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                String password = rs.getString(2);
                list.add(new User(name, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        DBExecuteStatus b = UserServices.login("1", "2");
        System.out.println(b.name());
        User user = new User("33", "22");
        b = UserServices.addUser(user);
        System.out.println(b);
    }
}
