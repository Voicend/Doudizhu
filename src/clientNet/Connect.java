package clientNet;
import java.net.Socket;
public class Connect {
	public static final String IP_ADDR = null;
	public static final int PORT = 8888;
	private static Socket socket;
	public static SendMes sendMes;

	public static void connect(String mes){
		try {
			socket = new Socket(IP_ADDR, PORT);
		} catch (Exception e) {
			System.out.println("连接服务器错误"); 
		}
		
		sendMes=new SendMes(socket);
		sendMes.setMsg(mes);
		sendMes.start();
	}
}
