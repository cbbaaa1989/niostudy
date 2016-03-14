package pers.fengyitian.server;

import java.io.IOException;
import java.nio.ByteBuffer;

public class StringContent implements Content{
	private String content;
	
	public StringContent(String content){
		
		this.content = content;
	}
	
	public StringContent(Exception e){
		this(e.toString());
	}

	@Override
	public void prepare() throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean send(ChannelIO cio) throws IOException {
		int sendSize = cio.write(ByteBuffer.wrap(content.getBytes()));
		return sendSize < content.length();
	}
	@Override
	public void release() throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String type() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long length() {
		// TODO Auto-generated method stub
		return 0;
	}

}
