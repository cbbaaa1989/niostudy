package pers.fengyitian.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.spec.EncodedKeySpec;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	/**
	 * 端口号
	 */
	private int port;

	/**
	 * 运行状态
	 */
	private volatile boolean running;
	
	private ServerSocketChannel ssc;
	
	private Selector selector = null;

	private ExecutorService executorService = Executors.newCachedThreadPool();
	public Server(int port) throws IOException{
		this.port = port;
		ssc = ServerSocketChannel.open();
		ssc.bind(new InetSocketAddress(port));
		selector = Selector.open();
		ssc.configureBlocking(false);
	}
	
	
	public void service() throws IOException{
		ssc.register(selector, SelectionKey.OP_ACCEPT, new AcceptHandler());//注册接受连接就绪事件
		for(;;){
			int n = selector.select();
			
			if(n == 0){
				continue;
			}
			
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = readyKeys.iterator();
			while(it.hasNext()){
				SelectionKey key = null;
				
				try{
					key = (SelectionKey)it.next();
					it.remove();
					final pers.fengyitian.server.Handler handler = (pers.fengyitian.server.Handler)key.attachment();
					handler.handle(key);
					
					
				}catch(IOException e){
					
					e.printStackTrace();
					try{
						if(key != null){
							key.cancel();
							key.channel().close();
						}
					}catch(IOException ex){
						ex.printStackTrace();
					}
					
				}
				
				
			}
		}
		
	}
	
	public void start(){
		
		try {
			SocketAddress localSocketAddress = new InetSocketAddress(port);
			ssc = ServerSocketChannel.open();
			ssc.bind(localSocketAddress);
			
			while(running){
				
				final SocketChannel socketChannel = ssc.accept();
				socketChannel.configureBlocking(false);
				
				executorService.submit(new Runnable() {
					
					public void run() {
						
						try {
							ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
							byte[] buf = new byte[1024];
							socketChannel.read(byteBuffer);
							byteBuffer.flip();
							if(byteBuffer.hasRemaining()){
								
							}
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	class Handler implements Runnable{
		
		private SocketChannel socketChannel;
		
		public Handler(SocketChannel socketChannel){
			this.socketChannel = socketChannel;
		}

		public void run() {

			try{
				Socket socket = socketChannel.socket();
				System.out.println("接收到客户连接，来自" + socket.getInetAddress() + ":" + socket.getPort());
				ByteBuffer buffer = ByteBuffer.allocate(1024);//接受http请求，假设不超过1024字节
				socketChannel.read(buffer);
				buffer.flip();
				String request = decode(buffer);
				System.out.println(request);//打印http请求
				
				//生成http响应结果
				StringBuilder sb = new StringBuilder("HTTP/1.1 200 OK \r\n");
				sb.append("Content-Type:text/html\r\n\r\n");
				socketChannel.write(encode(sb.toString()));//发送http响应的第一行和响应头
				
				FileInputStream in;
				String firstLineOfRequest = request.substring(0,request.indexOf("\r\n"));
				if(firstLineOfRequest.indexOf("login.htm") != -1 ){
					in = new FileInputStream("login.htm");
				}else{
					in = new FileInputStream("hello.htm");
				}
				
				FileChannel fileChannel = in.getChannel();
				fileChannel.transferTo(0, fileChannel.size(), socketChannel);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try{
					if(socketChannel != null){
						socketChannel.close();//关闭连接
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
		
		private Charset charset = Charset.forName("GBK");
		
		/**
		 * 
		 * 解码
		 * @param buffer
		 * @return
		 */
		public String decode(ByteBuffer buffer){
			try {
				Request request = Request.parse(buffer);
				return request.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		
		/**
		 * 编码
		 * @param str
		 * @return
		 */
		public ByteBuffer encode(String str){
			return ByteBuffer.wrap(str.getBytes());
		}
		
		
		
	}
}
