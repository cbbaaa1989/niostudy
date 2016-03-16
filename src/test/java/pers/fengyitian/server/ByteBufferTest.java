package pers.fengyitian.server;

import static org.junit.Assert.*;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.junit.Test;

public class ByteBufferTest {

	@Test
	public void test() {

		try {
			String testStr = "冯义天测试";
			
			CharBuffer charBuffer =  CharBuffer.wrap(testStr);
			ByteBuffer byteBuffer = ByteBuffer.wrap(testStr.getBytes());
			byte[] bytes = testStr.getBytes("utf-8");
			
			
			for(int i = 0; i< bytes.length ; i++){
				System.out.println(i + ".    " + bytes[i] + "    " + (char)bytes[i]);
			}
			
			while(charBuffer.hasRemaining()){
				System.out.println("position:" +charBuffer.position());
				System.out.println("limit:" +charBuffer.limit());
				System.out.println("capacity:" + charBuffer.capacity());
				System.out.print(charBuffer.get() + " ");
			}
			System.out.println();
			while(byteBuffer.hasRemaining()){
				System.out.println("byteBuffer position:" +byteBuffer.position());
				System.out.println("byteBuffer limit:" +byteBuffer.limit());
				System.out.println("byteBuffer capacity:" + byteBuffer.capacity());
				System.out.print((char)byteBuffer.get() + " ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
