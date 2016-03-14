package pers.fengyitian.server;


/**   
 * @Description: 
 * @author lp3331  
 * @date 2016年3月14日 下午4:32:49 
 * @version V1.0   
 */
public interface Content extends Sendable{

	/**
	 * 正文的类型
	 * @return
	 */
	String type();
	
	/**
	 * 返回正文的长度
	 * 在正文还没有准备之前，即还没有调用prepare()方法之前，length()方法返回-1
	 * @return
	 */
	long length();
}
