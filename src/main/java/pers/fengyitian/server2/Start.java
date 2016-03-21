package pers.fengyitian.server2;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.net.SocketOptions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Start {

	
	public static void main(String[] args){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					
					ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
					serverSocketChannel.bind(new InetSocketAddress(8080));
					serverSocketChannel.configureBlocking(false);
					
					Selector selector = Selector.open();
					
					serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
					
					while(true){
						//System.out.println("server start select");
						int n = selector.select();
						System.out.println("server select number : " + n);
						if(n > 0){
							System.out.println("server select number : " + n);
							Set<SelectionKey> selectedKeys = selector.selectedKeys();
							Iterator< SelectionKey> it = selectedKeys.iterator();
							while(it.hasNext()){
								SelectionKey key = it.next();
								
								if(key.isAcceptable()){
									
									ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
									SocketChannel socketChannel = ssc.accept();
									if(socketChannel != null){
										System.out.println("server accept one socket : " + 
									"port : " + socketChannel.socket().getPort());
										socketChannel.configureBlocking(false);
										socketChannel.register(selector, SelectionKey.OP_READ);
										System.out.println("???????????");
									}
									it.remove();
								}
								if(key.isReadable()){
									System.out.println("server start read ....");
									SocketChannel socketChannel = (SocketChannel)key.channel();
									ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
									
									
									
									
									 while(read(socketChannel, byteBuffer, selector) > 0){
										 System.out.println("read something .....");
										while(byteBuffer.hasRemaining()){
											//char c = (char)byteBuffer.get();
											//if(c == 'q'){

												//socketChannel.register(selector, key.interestOps() | ~ SelectionKey.OP_READ);
												System.out.println("server start to write .....");
												byteBuffer.flip();
												socketChannel.write(byteBuffer);
												
//												socketChannel.register(selector, SelectionKey.OP_READ);
//												selector.wakeup();
												it.remove();
											//}
										}
										
										if(byteBuffer.limit() == byteBuffer.capacity()){
											
											ByteBuffer.allocate(byteBuffer.capacity() * 2).put(byteBuffer);
										}
									}
									

								}
								
								//it.remove();
								System.out.println("end ....");
							}
						}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost",8080));
					ByteBuffer readBuf = ByteBuffer.allocate(1024);
					//readBuf.order(ByteOrder.BIG_ENDIAN);
					BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)) ;
					while(true){
						try {
							String str = reader.readLine();
							System.out.println(str.getBytes());
							System.out.println("system in read : " + str);
							if(str.equals("exit")){
								return ;
							}
							long startWrite = System.currentTimeMillis();
							int writen = socketChannel.write(ByteBuffer.wrap(str.getBytes()));
							long endWrite = System.currentTimeMillis();
							System.out.println("client write time : " + (endWrite - startWrite) + 
									" .write byte number : " + writen);
							readBuf.clear();
							System.out.println("1. " + readBuf);
							int readn = socketChannel.read(readBuf);
							System.out.println("client read time : " + (System.currentTimeMillis() - endWrite)
									+ " read number is : " + readn);
							System.out.println("2. " + readBuf);
							
							readBuf.flip();
							
							System.out.println("read from server is : " + new String(readBuf.array()));
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		}).start();
		
		
	}
	
	private static int read(SocketChannel socketChannel,ByteBuffer byteBuffer,Selector selector) throws IOException{
		int n = socketChannel.read(byteBuffer);
		return n;
	}
}
