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

		if(requestReceived) return true;//如果已经接受到http请求的所有数据，就返回true
		
		if((channelIO.read() < 0) || Request.isComplete(channelIO.getReadBuf())){
			
			requestByteBuffer = channelIO.getReadBuf();
			
			return requestReceived = true;
		}
		return false;
	}
	
	private boolean parse() throws IOException{
		try{
			request = Request.parse(requestByteBuffer);
			
			return true;
		}catch(Exception e){
			response = new Response(Response.Code.BAD_REQUEST, new StringContent(e));
		}
		
		return false;
	}
	
	
	private void build() throws IOException{
		
		Request.Action action = request.action();
		
		if(action != Request.Action.GET && action != Request.Action.HEAD){
			
			response = new Response(Response.Code.METHOD_NOT_ALLOWED,new StringContent("Method Not Allowed"));
		}else{
			response = new Response(Response.Code.OK, new FileContent(request.uri()), action);
		}
	}
	
	/**
	 * 接收http请求，发送http响应
	 */
	@Override
	public void handle(SelectionKey sk) throws IOException {
		
		try{
			if(request == null){
				
				//接收请求
				if(!receive(sk)){
					return ;
				}
				requestByteBuffer.flip();
				
				//如果成功解析了http请求，就创建一个response对象
				if(parse()){
					build();
				}
				
				try{
					response.prepare();
				}catch(IOException x){
					
					response.release();
					
					response = new Response(Response.Code.NOT_FOUND,new StringContent(x));
				}
				
				if(send()){
					
					//如果http响应没有发送完毕，就需要注册写就绪事件，
					//以便在写就绪事件发生时继续发送数据
					sk.interestOps(SelectionKey.OP_WRITE);
				}else{
					//如果http响应发送完毕，就断开底层的连接，
					//并释放response占用的资源
					
					channelIO.close();
					response.release();
				}
				
				
			}else{//如果已经接受到http请求的所有数据
				
				if(!send()){//如果http响应发送完毕
					
					channelIO.close();
					response.release();
				}
			}
		}catch(IOException e){
			
			e.printStackTrace();
			channelIO.close();
			if(response != null){
				response.release();
			}
		}
	}
	/**
	 * 发送http响应，如果全部发送完毕，就返回false,否则返回true
	 * @return
	 * @throws IOException
	 */
	private boolean send() throws IOException{
			return response.send(channelIO);
	}

}
