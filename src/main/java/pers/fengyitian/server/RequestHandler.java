package pers.fengyitian.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public class RequestHandler implements Handler{

	private ChannelIO channelIO;
	
	private ByteBuffer requestByteBuffer = null;//存放http请求的缓冲区
	
	private boolean requestReceived = false;//表示是否已经接收到http请求的所有数据
	
	private Request request = null;//表示http请求
	
	private Response response = null;//表示http响应

	public RequestHandler(ChannelIO channelIO){
		this.channelIO = channelIO;
	}
	
	private boolean receive(SelectionKey sk) throws IOException{
		
		ByteBuffer tmp = null;
		
		if(requestReceived) return true;//如果已经接受到http请求的所有数据，就返回true
		
		if((channelIO.read() < 0) || Req){
			
		}
	}
	
	
	public void handle(SelectionKey key) throws IOException {
		
	}

}
