package client;

import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.net.*;

public class ClientChatFrame extends Frame {
    TextField tfTxt = new TextField();

    TextArea taContent = new TextArea();
    boolean bConnected = false;
    Socket s = null;
    DataOutputStream dos = null;
    DataInputStream dis = null;
    Thread tRecv = new Thread(new RecvThread());


    public static void main(String[] args) {
        new ClientChatFrame().launchFrame();
    }

    public void launchFrame() {
    	setLocation(400, 300);
	this.setSize(300, 300);//this是必须的吗
	add(tfTxt, BorderLayout.SOUTH);
	add(taContent, BorderLayout.NORTH);
	pack();
	this.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		disconnect();
	        System.exit(0);
	    }
	});
	tfTxt.addActionListener(new TFListener());
	setVisible(true);
	connect();
	tRecv.start();
    }

    public void connect() {
    	try{
	    s = new Socket("127.0.0.1", 8888);
	    dos = new DataOutputStream(s.getOutputStream());
	    dis = new DataInputStream(s.getInputStream());
	    bConnected = true;
	    System.out.println("connect...");
	}catch (UnknownHostException e) {
	    e.printStackTrace();
	}catch (IOException e) {
            e.printStackTrace();
	}
    }

    public void disconnect() {
	 try {
                dos.close();
                dis.close();
                s.close();
           }catch(IOException e) {
               e.printStackTrace();
           }

	    /*
	try{
	    bConnected = false;
	    tRecv.join(); // 合并线程到主线程，然后能正常关闭
	    // 但是readUTF方法阻塞，持续等待。。。。
	}catch(InterruptedException e) {
	    e.printStackTrace();
	}finally {
	   try {
                dos.close();
	        dis.close();
       	        s.close();
	   }catch(IOException e) {
	       e.printStackTrace();
	   }
	}
	*/
    }

    private class TFListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
	    String str = tfTxt.getText().trim();
	    //taContent.setText(str);
	    tfTxt.setText("");
	    try{
		dos.writeUTF(str);
		dos.flush();
		//dos.close();
	    }catch(IOException e1) {
	        e1.printStackTrace();
	    }
	}
    }

    private class RecvThread implements Runnable {
    
	public void run() {
	    try{
	        while(bConnected){
		    String str = dis.readUTF();
		    taContent.setText(taContent.getText()+str+"\n");
		}
	    }catch(SocketException e) {// 故作迷章
	        System.out.println("client closed");
	    }catch(IOException e) {
	        e.printStackTrace();
	    }
	}
    
    }
}
