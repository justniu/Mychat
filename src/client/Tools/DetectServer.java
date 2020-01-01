package client.Tools;

import client.UI.LoginFrame;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

public class DetectServer implements Runnable{
    private static String serverIP;
    private volatile static boolean flag = false;
    private LoginFrame loginFrame=null;

    public DetectServer(){}

    public DetectServer(LoginFrame loginFrame){
        this.loginFrame = loginFrame;
    }

     public void run(){
        new ReceiveThread().start();
         try {
             Thread.sleep(100);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         broadPack();
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
            try {
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                DatagramSocket ds = new DatagramSocket(9999);
                ds.setSoTimeout(3500);
                ds.receive(dp);
                serverIP = dp.getAddress().toString().substring(1);
                flag = true;
                ds.close();
            } catch (SocketException e) {
            } catch (SocketTimeoutException e){
                JOptionPane.showMessageDialog(loginFrame, "Connection timed out Please try again", "Timeout", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
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
