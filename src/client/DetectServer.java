package client;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;

public class DetectServer implements Runnable{
    private static String serverIP;
    private volatile static boolean flag = false;
    private LoginFrame loginFrame;

    public DetectServer(){}

    public DetectServer(LoginFrame loginFrame){
        this.loginFrame = loginFrame;
    }

     public void run(){
        new ReceiveThread().start();
        broadPack();
        JOptionPane.showMessageDialog(loginFrame, "connected to "+getServerIP(), "connected", JOptionPane.INFORMATION_MESSAGE);
    }


    public void broadPack() {

        byte[] buf = "hello".getBytes();
        DatagramPacket pack = new DatagramPacket(buf, buf.length, new InetSocketAddress("255.255.255.255", 12345));
        try {
            DatagramSocket ds = new DatagramSocket(8989);
            ds.send(pack);
            ds.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ReceiveThread extends Thread{
        public void run(){
            byte[] buf = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            try {
                DatagramSocket ds = new DatagramSocket(9999);
                ds.receive(dp);
                serverIP = dp.getAddress().toString().substring(1);
                flag = true;
                ds.close();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getServerIP(){
        while(!flag);
        return serverIP;
    }

    public static void main(String[] args) {
        new Thread(new DetectServer()).start();
    }
}
