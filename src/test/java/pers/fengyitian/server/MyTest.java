package pers.fengyitian.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

public class MyTest {

	@Test
	public void test(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				byte[] buf = new byte[1];
				int readN = 0;
				try {
					ServerSocket serverSocket = new ServerSocket(80);
					while (true) {

						Socket socket = serverSocket.accept();
						
						InputStream inputStream = socket.getInputStream();
						
						while(true){
							int n = inputStream.read(buf,readN,buf.length - readN);
							readN += n;
							if(readN == buf.length){
								byte[] newBuf = new byte[buf.length * 2];
								System.arraycopy(buf, 0, newBuf, 0, buf.length);
								buf = newBuf;
							}else if(readN <= 0){
								break;
							}
						}
						System.out.println(new String(buf,"utf-8") );
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
		try {
			StringBuilder str = new StringBuilder();
			
			for(int i =0 ; i< 100 ;i++){
				str.append("测试啊测试123ABS冯义天");
			}
			
			Socket socket = new Socket("localhost", 80);
			socket.getOutputStream().write(str.toString().getBytes());
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
