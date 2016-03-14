package pers.fengyitian.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class ChannelIO {

	protected SocketChannel socketChannel;
	
	protected ByteBuffer requestBuffer; //存放请求数据
	
	private static int requestBufferSize = 4096;
	
	public ChannelIO(SocketChannel socketChannel,boolean blocking) throws IOException{
		this.socketChannel = socketChannel;
		
		socketChannel.configureBlocking(blocking);
		
		requestBuffer = ByteBuffer.allocate(requestBufferSize);
		
	}
	
	public SocketChannel getSocketChannel(){
		return socketChannel;
	}
	
	protected void resizeRequestBuffer(int remaining){
		
		if(requestBuffer.remaining() < remaining){
			//把容量扩大为原来的两倍
			
			ByteBuffer bb = ByteBuffer.allocate(requestBuffer.capacity() * 2);
			
			requestBuffer.flip();
			bb.put(requestBuffer) ;//把原来缓冲区中的数据复制到新的缓冲区
			requestBuffer = bb;
		}
	}
	
	/**
	 * 接收数据，就把他们存放到requestBuffer中，如果requestBuffer的剩余容量不足5%,
	 * 就通过resizeRequestBuffer()方法扩充容量
	 * @return
	 * @throws IOException
	 */
	public int read() throws IOException{
		resizeRequestBuffer(requestBufferSize / 20);
		return socketChannel.read(requestBuffer);
	}
	
	/**
	 * 返回requestBuffer,他存放了请求数据
	 * @return
	 */
	public ByteBuffer getReadBuf(){
		return requestBuffer;
	}
	
	/**
	 * 发送参数指定的bytebuffer中的数据
	 * @return
	 * @throws IOException
	 */
	public  int write(ByteBuffer src) throws IOException{
		
		return socketChannel.write(src);
		
	}
	/**
	 * 把filechannel里的数据写到socketchannel中
	 * @param fc
	 * @param pos
	 * @param len
	 * @return
	 * @throws IOException
	 */
	public long transferTo(FileChannel fc,long pos,long len) throws IOException{
		
		return fc.transferTo(pos, len, socketChannel);
	}
	
	public void close() throws IOException{
		socketChannel.close();
	}
}
