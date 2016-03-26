package pers.fengyitian.server2;

public class ParseStateMachine {

	public final static int method = 1;

	public final static int url = 2;
	
	public final static int protocol = 3;
	
	public final static int header = 4;
	
	public int state = 1;
	
	public int state(){
		return state;
	}
	
	public void forward(){
		state ++;
	}
}
