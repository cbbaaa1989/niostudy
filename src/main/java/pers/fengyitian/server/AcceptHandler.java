package pers.fengyitian.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AcceptHandler implements Handler{

	public void handle(SelectionKey key) throws IOException {

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		//在非阻塞模式下,serversocketchannel.accept()可能会返回Null
		//判断socketchannel是否为null,可以使程序更加健壮，避免nullpointerexception
		SocketChannel socketChannel = serverSocketChannel.accept();
		if(socketChannel == null){
			return ;
		}
		
		System.out.println("接收到客户端请求，来自:" + socketChannel.socket().getInetAddress() + ":" 
				+ socketChannel.socket().getPort());
		ChannelIO cio = new ChannelIO(socketChannel,false);
		
	}

}
