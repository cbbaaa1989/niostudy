package pers.fengyitian.server2;

public class ParseStateMachine {

	public final static int method = 1;

	public final static int url = 2;
	
	public final static int version = 3;
	
	public final static int header_name = 4;
	
	public final static int header_value = 5;
	
	public final static int endLine = 6;
	
	//public final static int endLine_n = 6;
	
	public int state = 1;
	
	public void goForward(){
		state ++;
	}
}
