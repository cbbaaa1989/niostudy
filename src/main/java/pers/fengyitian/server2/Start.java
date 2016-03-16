package pers.fengyitian.server2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class Start {

	
	public static void main(String[] args){
		
		
		try{
			
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(8080));
			serverSocketChannel.configureBlocking(false);
			
			Selector selector = Selector.open();
			
			serverSocketChannel.register(selector, SelectionKey.OP_READ);
			
			while(true){
				int n = selector.selectNow();
				if(n > 0){
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					while(selectedKeys.iterator().hasNext()){
						SelectionKey key = selectedKeys.iterator().next();
						
						if(key.isAcceptable()){
							
							ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
							SocketChannel socketChannel = ssc.accept();
							socketChannel.configureBlocking(false);
							socketChannel.register(selector, SelectionKey.OP_READ);
						}
						if(key.isReadable()){
							SocketChannel socketChannel = (SocketChannel)key.channel();
							ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
							
							byteBuffer.flip();
							
							
							 while(read(socketChannel, byteBuffer, selector) > 0){
								while(byteBuffer.hasRemaining()){
									char c = (char)byteBuffer.get();
									if(c == '\n'){
										int position = byteBuffer.position();
										if(byteBuffer.get(position - 3) == '\r'
										&& byteBuffer.get(position - 2) == '\n'
										&& byteBuffer.get(position - 1) == '\r'){
											key.interestOps() & ~ SelectionKey.OP_READ;
										}
									}
								}
								
							}
							
							
							if(byteBuffer.limit() == byteBuffer.capacity()){
								
								ByteBuffer.allocate(byteBuffer.capacity() * 2).put(byteBuffer);
							}
						
							
						}
					}
				}
			}
			
		}catch(IOException ioe){
			
		}
		
		
	}
	
	private static int read(SocketChannel socketChannel,ByteBuffer byteBuffer,Selector selector) throws IOException{
		int n = socketChannel.read(byteBuffer);
		return n;
	}
}
