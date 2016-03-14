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
		
		RequestHandler rh = new RequestHandler(cio);
		//注册读就绪事件，把requesthandler作为附件
		//当这种事件发生时，将有requesthandler处理该事件
		socketChannel.register(key.selector(), SelectionKey.OP_READ, rh);
		
	}

}
