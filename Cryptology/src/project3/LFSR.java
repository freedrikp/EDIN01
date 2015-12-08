package project3;

import java.util.LinkedList;
import java.util.ArrayList;

public class LFSR {
	private int limit;
	private ArrayList<Integer> connection;
	private int startState;
	
	public LFSR(int limit, String connect){
		this.limit =  limit;
		connection = new ArrayList<Integer>();
		String[] connectionPol = connect.split(" ");
		for(int i = 0;i < connectionPol.length;i++){
			connection.add(Integer.parseInt(connectionPol[i]));
		}
	}
	public boolean setStartState(int startState){
		if ( startState  > Math.pow(2,limit)){
			return false;
		}
		this.startState = startState;
		return true;
	}
	public int step(){
		int feedback = 0;
		for(int i = 0; i < connection.size(); i ++){
			feedback ^= (startState>>connection.get(i)) & 1;
		}
		int output =(startState>>(limit-1)) & 1;
		startState <<=1;
		startState += feedback;
		return output;
	}
	public String getOutput(int startState,int length){
		this.startState = startState;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++){
			sb.append(step());
		}
		return sb.toString();
	}
}
