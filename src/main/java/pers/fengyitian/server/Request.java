package pers.fengyitian.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**   
 * @Description: 
 * @author lp3331  
 * @date 2016年3月14日 下午2:40:14 
 * @version V1.0   
 */
public class Request {
	static class Action{
		
		private String name;
		
		private Action(String name){this.name = name;}
		
		public String toString(){
			return name;
		}
		
		static Action GET = new Action("GET");
		
		static Action PUT = new Action("PUT");
		
		static Action POST = new Action("POST");
		
		static Action HEAD = new Action("HEAD");
		
		public static Action parse(String s){
			
			if(s.equals("GET")){
				return GET;
			}
			if(s.equals("PUT")){
				return PUT;
			}
			if(s.equals("POST")){
				return POST;
			}
			if(s.equals("HEAD")){
				return HEAD;
			}
			throw new IllegalArgumentException(s);
		}
	}
	

	 private Action action;
	 
	 private String version;
	 
	 private URI uri;
	 
	 public Action action(){
		 
		 return action;
	 }
	 
	 public String version(){
		 
		 return version;
		 
	 }
	 
	 public URI uri(){
		 
		 return uri;
	 }
	 
	 private Request(Action a,String v,URI uri){
		 
		 this.action = a;
		 
		 this.version = v;
		 
		 this.uri = uri;
		 
	 }
	 
	 public String toString(){
		 
		 return (action + "" + version + "" + uri);
	 }
	 
	 private static Charset requestCharset = Charset.forName("GBK");
	 
	 /**
	  * 判断butebuffer是否包含了http请求的所有数据
	  * http请求以"\r\n\r\n"结尾
	  */
	 
	 public static boolean isComplete(ByteBuffer bb){
		 
		 ByteBuffer temp = bb.asReadOnlyBuffer();
		 
		 temp.flip();
		 
		 String data = requestCharset.decode(temp).toString();
		 
		 if(data.indexOf("\r\n\r\n") != -1 ){
			 
			 return true;
		 }
		 
		 return false;
		 
		 
	 }
	 
	 
	 private static ByteBuffer deleteContent(ByteBuffer bb){
		 
		 ByteBuffer temp = bb.asReadOnlyBuffer();
		 
		 String data = requestCharset.decode(temp).toString();
		 
		 if(data.indexOf("\r\n\r\n") != -1 ){
			 
			 data = data.substring(0,data.indexOf("\r\n\r\n") + 4);
			 
			 return requestCharset.encode(data);
		 }
		 
		 return bb;
		 
	 }
	 
	 
	 
	 public static Request parse(ByteBuffer bb) throws Exception{
		 
		 bb = deleteContent(bb);//删除请求正文
		 
		 CharBuffer cb = requestCharset.decode(bb);//解码
		 
		 Matcher m = requestPattern.matcher(cb);//进行字符串匹配
		 
		 //如果http请求与指定的字符串模式不匹配，说明请求数据不正确
		 /*if(!m.matches()){
			 System.out.println("http请求与指定的字符串模式不匹配");
			 throw new Exception();
		 }*/

		 Action a;
		 
		 try{
			 a = Action.parse(m.group(1));
			 
		 }catch(IllegalArgumentException x){
			 
			 throw new Exception();
		 }
		 
		 URI u;
		 
		 try{
			 u = new URI("http://"
					 + m.group(4)
					 + m.group(2));
		 }catch(URISyntaxException x){
			 
			 throw new Exception();
		 }
		 //创建一个Request对象，并将其返回
		 return new Request(a, m.group(3), u);
		 
		 
		 
		 
		 
	 }
	 
	 /**
	  * 设定用于解析HTTP请求的字符串匹配模式。对于以下形式的http请求
	  * 
	  *  GET /dir/file HTTP/1.1
	  *  Host:hostname
	  *  
	  *  将被解析成
	  *  
	  *  group[1] = "GET"
	  *  group[2] = "/dir/file"
	  *  group[3] = "1.1"
	  *  group[4] = "hostname"
	  */
	 private static Pattern requestPattern = 
			 
			 Pattern.compile("\\A([A-Z]+)+([^ ]+)+HTTP/([0-9\\.]+)$"
					 			+ ".*^Host:([^ ]+)$.*\r\n\r\n\\z",
					 			Pattern.MULTILINE | Pattern.DOTALL);
	 
	 public static void main(String args[]){
		 String httpRequest = 
				"GET /index.html HTTP/1.1\r\n" +
				"Host: localhost:8080\r\n" +
				"User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:43.0) Gecko/20100101 Firefox/43.0\r\n" +
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
				"Accept-Language: zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3\r\n" +
				"Accept-Encoding: gzip, deflate\r\n" +
				"Connection: keep-alive\r\n";
		 boolean match = Request.isComplete(ByteBuffer.wrap(httpRequest.getBytes()));
		 
		 System.out.println(match);
	 }
	 
	 
	 
	 
	 
	 
	 
}
