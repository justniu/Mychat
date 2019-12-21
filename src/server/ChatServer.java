package server;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {
    
    boolean started = false;
    ServerSocket ss = null;
    List<Client> clients = new ArrayList<Client>();

    public static void main(String[] args) {
	new ChatServer().start();
    }

    public void start() {
        try{
	    ss = new ServerSocket(7788);
	    started = true;
	}catch (BindException e) {
	    System.out.println("port is used!");
	    System.exit(0);
	}catch (IOException e) {
	    e.printStackTrace();
	}

	try{
	    while(started) {
	       Socket s = ss.accept();
	       Client c = new Client(s);
	       System.out.println("a connect");
	       new Thread(c).start();
	       clients.add(c);
	    }
	}catch(IOException e) {
	    e.printStackTrace();
	}finally{
	    try{
		ss.close();
	    }catch (IOException e1) {
	        e1.printStackTrace();
	    }
	}
    }

    private class Client implements Runnable {
        private Socket s;
	private ObjectInputStream ois = null;
        private boolean bConnected = false;	
	private ObjectOutputStream oos = null;
	
	public Client(Socket s) {
	    this.s = s;
	    try{
	    	ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
	        bConnected = true;
	    }catch(IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	public void send(String str) {
	    try{
	        oos.writeUTF(str);
	    }catch(IOException e){
		clients.remove(this);
		System.out.println("a client quit");
	    }
	}

	public void run() {
	       try{
         	   while(bConnected) {
	               String str = ois.readUTF();//EOFException
	               System.out.println(str);
		       for(int i = 0; i<clients.size(); i++) {
		           Client c = clients.get(i);
			   c.send(str);
		       }
	           }
	       }catch (EOFException e) {
	           System.out.println("Client closed!");
	       }catch (IOException e) {
		   e.printStackTrace();
	       }finally {
	       	   try{
		       if(ois != null) ois.close();
		       if(oos != null) oos.close();
		       if(s != null) s.close();
		   }catch (IOException e) {
		       e.printStackTrace();
		   }
	       }
	}
    }
}
