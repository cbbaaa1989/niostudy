package pers.fengyitian.echoserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**   
* @Description: 
* @author lp3331  
* @date 2016年8月8日 下午5:14:41 
* @version V1.0   
*/
public class EchoServer {
	
	private volatile boolean running = false;
	
	private Queue<SocketChannel> acceptedQueue = new LinkedBlockingQueue<SocketChannel>();
	
	private Queue<SelectionKey> keyQueue = new LinkedBlockingQueue<SelectionKey>();
	
	public void startUp(){
		running = true;
	}
	
	class Accepter extends Thread{
		
		Selector selector ;
		
		ServerSocketChannel ssc ;
		
		public Accepter() throws IOException{
		
			selector = Selector.open();
			
			ssc = ServerSocketChannel.open();
			
			ssc.bind(new InetSocketAddress("localhost", 9999));
			
			ssc.configureBlocking(false);
			
			ssc.register(selector, SelectionKey.OP_ACCEPT);
		}
		
		@Override
		public void run(){
			
			while(running && ssc.isOpen()){
				
				try {
					SocketChannel channel = ssc.accept();
					
					acceptedQueue.offer(channel);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			}
		}
	}
	
	
	class SelectThread extends Thread{
		
		Selector selector ;
		
		public SelectThread() throws IOException {
			
			selector = Selector.open();
		}
		
		@Override
		public void run(){
			
			while(running ){
				
				processAcceptedSockets();
			}
			
		}
		
		private void processAcceptedSockets() {
			SocketChannel sc = null;
			
			while(running && (sc = acceptedQueue.poll()) != null){
				
				if(!sc.isOpen()){
					return;
				}
				
				try {
					sc.register(selector, SelectionKey.OP_READ);
				} catch (ClosedChannelException e) {
					e.printStackTrace();
				}
			}
			
		}
	}


	

}
