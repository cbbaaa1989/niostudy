package pers.fengyitian.server2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;



/**   
 * @Description: 
 * @author lp3331  
 * @date 2016年3月17日 下午4:47:06 
 * @version V1.0   
 */
public class MyConsole {

	
	public static void main(String args[]){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)) ;
			CharBuffer charBuffer = CharBuffer.allocate(1024);
			
			outer : while(true){
//				String s = reader.readLine();
//				System.out.println(s);
//				
//				if(s.equals("q")){
//					break;
//				}
				int n = reader.read(charBuffer);
				
				//System.out.println("read number : " + n);
				if(n > 0){
					
					charBuffer.flip();
				}
				while(charBuffer.hasRemaining()){

					char c = charBuffer.get();
					System.out.print((int)c + ",");
					if(c == 'q'){
						break outer;
					}
				}
				charBuffer.limit(charBuffer.capacity());
				//charBuffer.compact();
				
			}
			System.out.println("end");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
