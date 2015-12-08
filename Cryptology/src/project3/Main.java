package project3;

import java.math.BigInteger;

public class Main {

	public static int hammingDistance(String x, String y){
		int sum = 0;
		for (int i = 0; i < x.length(); i++) { //both are of the same length
		    if (x.charAt(i) != y.charAt(i)) {
		        sum += 1;
		    }
		}
		return sum;
	}
	public static int[] bestResult(LFSR test, String output){
		int[] result = new int[3];
		int bestDistance = Integer.MAX_VALUE;
		int startState = 0;
		int bestStartState = startState;	
		int length = output.length();
		while(test.setStartState(startState)){
			String x = test.getOutput(startState,length);
			int currentDistance = hammingDistance(x,output);
			if (currentDistance < bestDistance){
					bestDistance = currentDistance;	
					bestStartState = startState;
			};
			startState+=1;
		}
		result[0] = bestStartState;
		result[1] = bestDistance;
		return result;
	}
	public static void printResult(int[] result){
		System.out.println("best start state:	"+ result[0] +"	best distance: "+result[1]);
	}
	private static char compareChar(char i, char j, char k){
		return i+j+k-'0'*3 > 1 ? '1':'0';
	}
	public static boolean compare(String s1, String s2, String s3, String output){
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < output.length(); i++){
			sb.append(compareChar(s1.charAt(i), s2.charAt(i), s3.charAt(i)));
		}
		return sb.toString().equals(output);
	}
	public static void main(String[] args) {
		LFSR lfsr13 = new LFSR(13,"0 1 3 5 6 9 10 12");
		LFSR lfsr15 = new LFSR(15,"1 3 5 6 9 10 12 14");
		LFSR lfsr17 = new LFSR(17,"1 3 4 7 9 12 15 16");
		String output = "0001101001110010001110101101101110000010101101100111100111010111111100010100011100100001101111010000110011001000000011110001111000011100100111111001111001010010010010010010001111101001000111100";
		int[] result13 = bestResult(lfsr13,output);
		int[] result15 = bestResult(lfsr15,output);
		int[] result17 = bestResult(lfsr17,output);
		printResult(result13);
		printResult(result15);
		printResult(result17);
		
		String output13 = lfsr13.getOutput(result13[0], output.length());
		String output15 = lfsr15.getOutput(result15[0], output.length());
		String output17 = lfsr17.getOutput(result17[0], output.length());
		
		System.out.println("The answer is :	" + compare(output13,output15,output17,output));
	}
}