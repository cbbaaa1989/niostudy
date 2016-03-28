package pers.fengyitian.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;


/**   
 * @Description: 
 * @author lp3331  
 * @date 2016年3月28日 上午11:27:22 
 * @version V1.0   
 */
public class TestSocketRead {

	@Test
	public void test() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				ServerSocket serverSocket = null;
				try {
					serverSocket = new ServerSocket(8080);
					while(true){
						Socket socket = serverSocket.accept();
						
						if(socket != null){
							
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									try{
										
										byte[] buf = new byte[4096];
										
										InputStream input = socket.getInputStream();
										while(true){
											int n = input.read(buf);
											System.out.println(new String(buf,"utf-8"));
											System.out.println("read number : " + n);
											if(n <= 0){
												break;
											}
										}
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							}).start();;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						if(serverSocket != null){
							
							serverSocket.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();;
		
		try {
			Socket socket = new Socket("localhost", 8080);
			
			socket.getOutputStream().write("testhaha".getBytes());
			
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
