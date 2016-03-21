package pers.fengyitian.server2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class NewConnect {

	
	public static void main(String[] args ){
		
		try {
			Socket socket = new Socket("localhost", 8080);
			System.out.println("connect to server ......哈哈哈哈哈");
			socket.getOutputStream().write("尼玛(｡･∀･)ﾉﾞ嗨".getBytes());
			//BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
			//char[] buf = new char[1024];
			//int n = reader.read(buf);
			
			//reader.close();
			socket.close();
			//socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
