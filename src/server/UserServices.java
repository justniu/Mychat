package server;

import common.UserStatus;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.List;

public class UserServices {
    private static int idCount = 3; //id

//    /** 新增用户 */
//    public void addUser(User user){
//        List<User> users = loadAllUser();
//        users.add(user);
//        saveAllUser(users);
//    }

    /** user login */
    public DBExecuteStatus login(String name, String password){
        PreparedStatement preSql;
        Connection con;
        String sqlStr;
        ResultSet rs;
        con = DBManager.connection("MyChat", "root", "niuzhuang");
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
                    preSql.setString(1, "LOGINED");
                    preSql.setString(2, name);
                    preSql.setString(3, password);
                    int ok = preSql.executeUpdate();
                    return DBExecuteStatus.OK;
                }else {
                    return DBExecuteStatus.LOGINED_ERROR;
                }
            }else{
                return DBExecuteStatus.NO_EXIST_ERROR;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                con.commit();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return DBExecuteStatus.ERROR;
    }

    /** 根据ID加载用户 */
//    public User loadUser(long id){
//        User result = null;
//        List<User> users = loadAllUser();
//        for (User user : users) {
//            if(id == user.getId()){
//                result = user;
//                break;
//            }
//        }
//        return result;
//    }
//
//
    /** 加载所有用户 */
    @SuppressWarnings("unchecked")
    public List<User> loadAllUser() {
        List<User> list = null;
        return list;
    }
//
//    private void saveAllUser(List<User> users) {
//        ObjectOutputStream oos = null;
//        try {
//            oos = new ObjectOutputStream(
//                    new FileOutputStream(
//                            DataBuffer.configProp.getProperty("dbpath")));
//            //写回用户信息
//            oos.writeObject(users);
//            oos.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//            IOUtil.close(oos);
//        }
//    }
//
//
//
//    /** 初始化几个测试用户 */
//    public void initUser(){
//        User user = new User("admin", "Admin", 'm', 0);
//        user.setId(1);
//
//        User user2 = new User("123", "yong", 'm', 1);
//        user2.setId(2);
//
//        User user3 = new User("123", "anni", 'f', 2);
//        user3.setId(3);
//
//        List<User> users = new CopyOnWriteArrayList<User>();
//        users.add(user);
//        users.add(user2);
//        users.add(user3);
//
//        this.saveAllUser(users);
//    }
//
//    public static void main(String[] args){
//        new UserService().initUser();
//        List<User> users = new UserService().loadAllUser();
//        for (User user : users) {
//            System.out.println(user);
//        }
//    }
    public static void main(String[] args) {
        DBExecuteStatus b = new UserServices().login("2", "2");
        System.out.println(b.name());
    }
}
