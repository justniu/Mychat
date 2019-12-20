package server;

import java.io.IOException;
import java.net.*;

public class SendIP implements Runnable{
    private String clientIP;

    public SendIP(){
        while(true){
            RecPack();
            sendIP(clientIP);
        }
    }

    public void run(){
        while(true){
            RecPack();
            sendIP(clientIP);
        }

    }

    public void sendIP(String clientIP){
        byte[] buf = "hello".getBytes();
        try {
            DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(clientIP), 9999);
            DatagramSocket ds = new DatagramSocket(9888);
            ds.send(dp);
            ds.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RecPack(){

        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        try {
            DatagramSocket ds = new DatagramSocket(12345);
            ds.receive(dp);
            System.out.println(dp.getAddress());
            this.clientIP = dp.getAddress().toString().substring(1);
            ds.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new SendIP();
    }
}
