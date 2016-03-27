package pers.fengyitian.server2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import pers.fengyitian.server2.http.Header;
import pers.fengyitian.server2.http.Header.HeaderName;
import pers.fengyitian.server2.http.HttpRequest;
import pers.fengyitian.server2.http.Method;
import pers.fengyitian.server2.http.Url;
import pers.fengyitian.server2.http.Version;

public class BioServer {

	
	public static void main(String args[]){
		
		try {
			ServerSocket serverSocket = new ServerSocket(8080);
			
			while(true){
				
				Socket socket = serverSocket.accept();
				
				new Thread(new Runnable() {
					private int bufSize = 32;
					private int readN = 0;
					private int offset = 0;
					private int start = 0 ;
					private int parseCursor = 0;
					private ParseStateMachine stateMachine = new ParseStateMachine();
					private HttpRequest request = new HttpRequest();
					
					private Header curHeader = null;
					@Override
					public void run() {
						try{
							if(socket != null){
								
								InputStream input = socket.getInputStream();
								byte[] buf = new byte[bufSize];
								request.setBuf(buf);
								request.setBufSize(bufSize);
								
								while(true){
									int n = input.read(buf);
									
									offset += n;
									
									if(offset == bufSize){
										bufSize *= 2;
										byte[] newBuf = new byte[bufSize];
										System.arraycopy(buf, 0, newBuf, 0, offset);
										request.setBuf(newBuf);
										request.setBufSize(bufSize);
									}
									
									
									for(int i = start; i < offset ; i++){
										
										switch (buf[i]) {
										case ' ':
											if(stateMachine.state == ParseStateMachine.method){
												Method method = new Method(parseCursor, i - 1);
												request.setMethod(method);
												parseCursor ++;
												stateMachine.goForward();
												
											}else if(stateMachine.state == ParseStateMachine.url){
												Url url = new Url(parseCursor, i - 1);
												request.setUrl(url);
												parseCursor ++;
												stateMachine.goForward();
											}
											break;
										case ':':
											if(stateMachine.state == ParseStateMachine.header_name){
												curHeader = new Header();
												HeaderName headerName = new HeaderName(parseCursor, i - 1);
												curHeader.setHeaderName(headerName);
												
											}
											break;
										case '\n':
											if(buf[i - 1] == '\r'){
												
												if(stateMachine.state == ParseStateMachine.version){
													Version version = new Version(parseCursor, i);
													request.setVersion(version);
													parseCursor ++;
													stateMachine.goForward();
												}
												
											}
											
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
