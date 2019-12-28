package server;

import java.net.ServerSocket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;

public class DataBuffer {
    // 服务器端套接字
    public static ServerSocket serverSocket;
    //在线用户的IO Map
    public static Map<String, SingleClientIO> onlineUserIOCacheMap;
    //在线用户Map
    public static List<String> onlineUsersList;
    // 已注册用户表的Model
    public static RegistedUserTableModel registedUserTableModel;
    // 当前在线用户表的Model
    public static OnlineUserTableModel onlineUserTableModel;
    //数据库初始化
    public static Connection con;


    static{
        onlineUserIOCacheMap = new ConcurrentSkipListMap<String,SingleClientIO>();
        onlineUsersList = new ArrayList<String>();
        onlineUserTableModel = new OnlineUserTableModel();
        registedUserTableModel = new RegistedUserTableModel();
        con = DBManager.connection("MyChat", "root", "niuzhuang");

    }

}
