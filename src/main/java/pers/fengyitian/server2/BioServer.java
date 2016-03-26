package pers.fengyitian.server2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import pers.fengyitian.server2.http.HttpRequest;
import pers.fengyitian.server2.http.Method;
import pers.fengyitian.server2.http.Url;

public class BioServer {

	
	public static void main(String args[]){
		
		try {
			ServerSocket serverSocket = new ServerSocket(8080);
			
			while(true){
				
				Socket socket = serverSocket.accept();
				
				new Thread(new Runnable() {
					private int bufSize = 32;
					private int offset = 0;
					private int start = 0 ;
					private int parseCursor = 0;
					private ParseStateMachine stateMachine = new ParseStateMachine();
					private HttpRequest request = new HttpRequest();
					@Override
					public void run() {
						try{
							if(socket != null){
								
								InputStream input = socket.getInputStream();
								byte[] buf = new byte[bufSize];
								
								while(true){
									int n = input.read(buf);
									
									offset += n;
									
									if(offset == bufSize){
										bufSize *= 2;
										byte[] newBuf = new byte[bufSize];
										System.arraycopy(buf, 0, newBuf, 0, offset);
									}
									
									
									for(int i = start; i < offset ; i++){
										
										switch (buf[i]) {
										case ' ':
											if(stateMachine.state() == ParseStateMachine.method){
												Method method = new Method(parseCursor, i);
												request.setMethod(method);
												parseCursor ++;
												stateMachine.forward();
												
											}else if(stateMachine.state() == ParseStateMachine.url){
												Url url = new Url(parseCursor, i);
												request.setUrl(url);
												parseCursor ++;
												stateMachine.forward();
											}
											break;
										case '\r':
											break;
										case '\n':
											break;
										default:
											break;
										}
									
									}
								}
								
							}
							
							
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}).start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}
