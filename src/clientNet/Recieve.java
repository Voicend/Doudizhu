package clientNet;
import Handler.Handler;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Recieve extends Thread {
    private Handler myHandler;

    public void setMyHandler(Handler myHandler) {
        this.myHandler = myHandler;
    }

    public static final int PORT = 8887;
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			while(true){
				Socket socket = serverSocket.accept(); 
				DataInputStream input = new DataInputStream(socket.getInputStream());  	//input为客户端发送来的数据流 
				String mes = (String) input.readUTF();
				myHandler.handleReciever(mes);
				//mes为客户端发来的String字符串，
				/*
				 * 对mes进行处理的部分
				 * if(id==0)
				 * else if(id==1)
				 * 
				 */
			}
		} catch (IOException e1) {
			 System.out.println(e1.getMessage());    
		}    
    }
 
}