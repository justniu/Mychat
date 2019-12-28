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
                }else if(type == ResponseType.TOSENDFILE){ //准备发送文件
                    toSendFile(response);
                }else if(type == ResponseType.AGREERECEIVEFILE){ //对方同意接收文件
                    sendFile(response);
                }else if(type == ResponseType.REFUSERECEIVEFILE){ //对方拒绝接收文件
                    Requests.appendTxt2MsgListArea("【文件消息】对方拒绝接收，文件发送失败！\n");
                }else if(type == ResponseType.RECEIVEFILE){ //开始接收文件
                    receiveFile(response);
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

    /** 发送文件 */
    private void sendFile(Response response) {
        final FileInfo sendFile = (FileInfo)response.getData("sendFile");

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        Socket socket = null;
        try {
            socket = new Socket(sendFile.getDestIp(),sendFile.getDestPort());//套接字连接
            bis = new BufferedInputStream(new FileInputStream(sendFile.getSrcName()));//文件读入
            bos = new BufferedOutputStream(socket.getOutputStream());//文件写出

            byte[] buffer = new byte[1024];
            int n = -1;
            while ((n = bis.read(buffer)) != -1){
                bos.write(buffer, 0, n);
            }
            bos.flush();
            synchronized (this) {
                Requests.appendTxt2MsgListArea("【文件消息】文件发送完毕!\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            IOUtil.close(bis,bos);
            SocketUtil.close(socket);
        }
    }

    /** 接收文件 */
    private void receiveFile(Response response) {
        final FileInfo sendFile = (FileInfo)response.getData("sendFile");

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(sendFile.getDestPort());
            socket = serverSocket.accept(); //接收
            bis = new BufferedInputStream(socket.getInputStream());//缓冲读
            bos = new BufferedOutputStream(new FileOutputStream(sendFile.getDestName()));//缓冲写出

            byte[] buffer = new byte[1024];
            int n = -1;
            while ((n = bis.read(buffer)) != -1){
                bos.write(buffer, 0, n);
            }
            bos.flush();
            synchronized (this) {
                Requests.appendTxt2MsgListArea("【文件消息】文件接收完毕!存放在["
                        + sendFile.getDestName()+"]\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            IOUtil.close(bis,bos);
            SocketUtil.close(socket);
            SocketUtil.close(serverSocket);
        }
    }

    /** 准备发送文件	 */
    private void toSendFile(Response response) {
        FileInfo sendFile = (FileInfo)response.getData("sendFile");

        String fromName = sendFile.getFromUser();
        String fileName = sendFile.getSrcName()
                .substring(sendFile.getSrcName().lastIndexOf(File.separator)+1);

        int select = JOptionPane.showConfirmDialog(this.currentFrame,
                fromName + " 向您发送文件 [" + fileName+ "]!\n同意接收吗?",
                "接收文件", JOptionPane.YES_NO_OPTION);
        try {
            RequestBody request = new RequestBody();
            request.setAttribute("sendFile", sendFile);

            if (select == JOptionPane.YES_OPTION) {
                JFileChooser jfc = new JFileChooser();
                jfc.setSelectedFile(new File(fileName));
                int result = jfc.showSaveDialog(this.currentFrame);

                if (result == JFileChooser.APPROVE_OPTION){
                    //设置目的地文件名
                    sendFile.setDestName(jfc.getSelectedFile().getCanonicalPath());
                    //设置目标地的IP和接收文件的端口
                    sendFile.setDestIp(ConManager.ip);
                    sendFile.setDestPort(ConManager.RECEIVE_FILE_PORT);

                    request.setAction("agreeReceiveFile");
//                    receiveFile(response);
                    Requests.appendTxt2MsgListArea("【文件消息】您已同意接收来自 "
                            + fromName +" 的文件，正在接收文件 ...\n");
                } else {
                    request.setAction("refuseReceiveFile");
                    Requests.appendTxt2MsgListArea("【文件消息】您已拒绝接收来自 "
                            + fromName +" 的文件!\n");
                }
            } else {
                request.setAction("refuseReceiveFile");
                Requests.appendTxt2MsgListArea("【文件消息】您已拒绝接收来自 "
                        + fromName +" 的文件!\n");
            }

            Requests.sendTextRequestOnly(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}