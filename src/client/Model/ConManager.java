package client.Model;

import client.Model.OnlineUserListModel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ConManager {

    /** 当前客户端的用户信息 */
    public static String currentUser;
    /** 在线用户列表 */
    public static List<String> onlineUsers;
    /** 当前客户端连接到服务器的套节字 */
    public static Socket clientSocket;
    /** 当前客户端连接到服务器的输出流 */
    public static ObjectOutputStream oos;
    /** 当前客户端连接到服务器的输入流 */
    public static ObjectInputStream ois;
    /** 在线用户JList的Model */
    public static OnlineUserListModel onlineUserListModel;

    private ConManager(){}
}
