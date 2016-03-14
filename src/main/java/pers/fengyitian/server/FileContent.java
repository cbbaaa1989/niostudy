package pers.fengyitian.server;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.channels.FileChannel;


/**   
 * @Description: 
 * @author lp3331  
 * @date 2016年3月14日 下午4:39:13 
 * @version V1.0   
 */
public class FileContent implements Content{

	//假定文件的根目录是“root,该目录应该位于classpath下
	private static File ROOT = new File("root");
	
	private File file;
	
	private FileChannel fileChannel = null;
	
	private long length = -1;//文件长度
	
	private long position = -1;//文件的当前位置
	
	private String type = null;
	
	public FileContent(URI uri){
		file = new File(ROOT,
					uri.getPath().replace('/', File.separatorChar));
	}
	
	
	public void prepare() throws IOException {

		if(fileChannel == null)
			fileChannel = new RandomAccessFile(file, "r").getChannel();
		
		length = fileChannel.size();
		position = 0;
	}

	/**
	 * 发送正文，如果发送完毕，就返回false,否则就返回true
	 */
	public boolean send(ChannelIO cio) throws IOException {

		if(fileChannel == null){
			throw new IllegalStateException();
		}
		
		if(position < 0){
			throw new IllegalStateException();
		}
		
		//如果发送完毕，就返回false
		if(position >= length){
			return false;
		}
		
		position += cio.transferTo(fileChannel, position, length - position);
		return position < length;
	}
	
	public void release() throws IOException {

		if(fileChannel != null){
			fileChannel.close();//关闭filechannel
			fileChannel = null;
		}
	}

	public String type() {
		if(type != null){
			return type;
		}
		String nm = file.getName();
		if(nm.endsWith(".html") || nm.endsWith(".htm")){
			
			type = "text/html;charset=iso-8859-1";//HTML网页
		}else if(nm.indexOf('.') < 0 || nm.endsWith(".txt")){
			
			type = "text/plain;charset=ios-8859-1";//纯文本文件
		}
		else{
			type = "application/octet-stream";//二进制文件
		}
		return type;
	}

	public long length() {
		return length;
	}

}
