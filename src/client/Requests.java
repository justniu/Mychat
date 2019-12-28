package client;

import common.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Requests{

    public static void connect(){
        DetectServer detectServer = new DetectServer();
        new Thread(detectServer).start();
        String ip = detectServer.getServerIP();
        try {
            ConManager.clientSocket = new Socket(ip, 7788);
            ConManager.oos = new ObjectOutputStream(ConManager.clientSocket.getOutputStream());
            ConManager.ois = new ObjectInputStream(ConManager.clientSocket.getInputStream());
            JOptionPane msg = new JOptionPane("connected to "+ip, JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = msg.createDialog(null, "连接成功");
            dialog.setAlwaysOnTop(true);
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            // 2秒后自动关闭
            Timer timer = new Timer(2 * 1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });
            timer.setRepeats(false);
            timer.start();

            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "连接服务器失败,请检查!","服务器未连上", JOptionPane.ERROR_MESSAGE);//否则连接失败
            System.exit(0);
        }

    }
    /** 发送请求对象,主动接收响应 */
    public static Response sendTextRequest(RequestBody Request) throws IOException {
        Response response = null;
        try {
            // 发送请求
            ConManager.oos.writeObject(Request);
            ConManager.oos.flush();
            System.out.println("客户端发送了请求对象:" + Request.getAction());

            if(!"exit".equals(Request.getAction())){
                // 获取响应
                response = (Response) ConManager.ois.readObject();
                System.out.println("客户端获取到了响应对象:" + response.getStatus());
            }else{
                System.out.println("客户端断开连接了");
            }
        } catch (IOException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return response;
    }

    /** 发送请求对象,不主动接收响应 */
    public static void sendTextRequestOnly(RequestBody RequestBody) throws IOException {
        try {
            ConManager.oos.writeObject(RequestBody); // 发送请求
            ConManager.oos.flush();
            System.out.println("客户端发送了请求对象:" + RequestBody.getAction());
        } catch (IOException e) {
            throw e;
        }
    }
    /** 把指定文本添加到消息列表文本域中 */
    public static void appendTxt2MsgListArea(String txt) {
        ClientChatFrame.msgListArea.append(txt);
        //把光标定位到文本域的最后一行
        ClientChatFrame.msgListArea.setCaretPosition(ClientChatFrame.msgListArea.getDocument().getLength());
    }

}
