package pers.fengyitian.server;

import java.io.IOException;


/**   
 * @Description: 
 * @author lp3331  
 * @date 2016年3月14日 下午4:34:52 
 * @version V1.0   
 */
public interface Sendable {
	
	/**
	 * 准备发送的内容
	 * @throws IOException
	 */
	void prepare() throws IOException;
	
	/**
	 * 利用通道发送部分内容，如果所有内容发送完毕，就返回false
	 * 如果还有内容未发送，就返回true
	 * 如果内容还没有准备好，就抛出illegalstatexception
	 * @return
	 * @throws IOException
	 */
	boolean send(ChannelIO cio) throws IOException;

	/**
	 * 当服务器发送内容完毕，就调用此方法，释放内容占用的资源
	 * @throws IOException
	 */
	void release() throws IOException;
}
