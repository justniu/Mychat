package client;

import com.mysql.cj.xdevapi.Client;
import common.*;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread extends Thread{
    private JFrame currentFrame;  //当前窗体

    public ClientThread(JFrame frame){
        currentFrame = frame;
    }

    public void run() {
        try {
            while (ConManager.clientSocket.isConnected()) {
                Response response = (Response) ConManager.ois.readObject();
                ResponseType type = response.getType();

                System.out.println("获取了响应内容：" + type);
                if (type == ResponseType.LOGIN) {
                    String newUser = (String)response.getData("loginUser");
                    ConManager.onlineUserListModel.addElement(newUser);

                    ClientChatFrame.onlineCountLbl.setText(
                            "在线用户列表("+ ConManager.onlineUserListModel.getSize() +")");
                    Requests.appendTxt2MsgListArea("【系统消息】用户"+ newUser + "上线了！\n");
                }else if(type == ResponseType.LOGOUT){
                    String newUser = (String)response.getData("logoutUser");
                    ConManager.onlineUserListModel.removeElement(newUser);

                    ClientChatFrame.onlineCountLbl.setText(
                            "在线用户列表("+ ConManager.onlineUserListModel.getSize() +")");
                    Requests.appendTxt2MsgListArea("【系统消息】用户"+ newUser + "下线了！\n");

                }else if(type == ResponseType.CHAT){ //聊天
                    Message msg = (Message)response.getData("txtMsg");
                    Requests.appendTxt2MsgListArea(msg.getMessage());
                }else if(type == ResponseType.BOARD){
                    Message msg = (Message)response.getData("txtMsg");
                    Requests.appendTxt2MsgListArea(msg.getMessage());
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}