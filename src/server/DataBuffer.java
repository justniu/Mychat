package server;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DataBuffer {
    // 服务器端套接字
    public static ServerSocket serverSocket;
    //在线用户的IO Map
    public static Map<String, SingleClientIO> onlineUserIOCacheMap;
    //在线用户Map
    public static List<String> onlineUsersList;
    //服务器配置参数属性集
    public static Properties configProp;
    // 已注册用户表的Model
    public static RegistedUserTableModel registedUserTableModel;
//    // 当前在线用户表的Model
    public static OnlineUserTableModel onlineUserTableModel;
//    // 当前服务器所在系统的屏幕尺寸
//    public static Dimension screenSize;
//
    static{
//        // 初始化
//        onlineUserIOCacheMap = new ConcurrentSkipListMap<Long,OnlineClientIOCache>();
        onlineUsersList = new ArrayList<String>();
//        configProp = new Properties();
//        registedUserTableModel = new RegistedUserTableModel();
        onlineUserTableModel = new OnlineUserTableModel();
//        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//
//        // 加载服务器配置文件
//        try {
//            configProp.load(Thread.currentThread()
//                    .getContextClassLoader()
//                    .getResourceAsStream("serverconfig.properties"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
