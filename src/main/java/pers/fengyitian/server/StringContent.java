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

	public void prepare() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public boolean send(ChannelIO cio) throws IOException {
		int sendSize = cio.write(ByteBuffer.wrap(content.getBytes()));
		return sendSize < content.length();
	}

	public void release() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public String type() {
		// TODO Auto-generated method stub
		return null;
	}

	public long length() {
		// TODO Auto-generated method stub
		return 0;
	}

}
