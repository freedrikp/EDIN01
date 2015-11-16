package project1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Primes1 {

	private static List<BigInteger> genPrimes(int nbrOfPrimes) {
		List<BigInteger> primes = new ArrayList<BigInteger>(nbrOfPrimes);
		BigInteger prime = BigInteger.ONE;
		boolean found;
		while (primes.size() < nbrOfPrimes) {
			found = false;
			for (BigInteger big : primes) {
				if (prime.mod(big).equals(BigInteger.ZERO)) {
					found = true;
					break;
				}
			}
			if (!found && !prime.equals(BigInteger.ONE)) {
				primes.add(prime);
			}
			prime = prime.add(BigInteger.ONE);
		}
		return primes;
	}

	private static List<BigInteger> genRs(int lValue, BigInteger nValue,
											List<BigInteger> primes, List<BitSet> m, 
											int block, int factor, int jump) {

		LinkedList<BigInteger> rs = new LinkedList<BigInteger>();
		int k = 1;
		int j = 1;
		boolean turn = false;
		int increased = 0;
		while (rs.size() < lValue) {
			BigInteger ki = new BigInteger(Integer.toString(k));
			BigInteger ji = new BigInteger(Integer.toString(j));
			BigInteger r = squareRoot(ki.multiply(nValue)).add(ji);
			BigInteger r2 = r.pow(2).mod(nValue);
			BitSet binrow = genBinaryRow(r2, primes);
			if (binrow != null && !m.contains(binrow)) {
				m.add(binrow);
				rs.add(r);
				System.out.println("Added: " + rs.size() + " K: " + ki + " J: "
						+ ji);
				if (rs.size() >= lValue) {
					return rs;
				}
			}
			if (turn) {
				k += jump;
			} else {
				j += jump;
			}
			increased += jump;
			if ( increased % block == 0){
				increased = 0;
				turn = !turn;
			}
		
		}
		return rs;
	}

//	public static BigInteger squareRoot(BigInteger x)
//			throws IllegalArgumentException {
//		if (x.compareTo(BigInteger.ZERO) < 0) {
//			throw new IllegalArgumentException("Negative argument.");
//		}
//		
//		if (x.equals(BigInteger.ZERO) || x.equals(BigInteger.ONE)) {
//			return x;
//		} // end if
//		BigInteger two = BigInteger.valueOf(2L);
//		BigInteger y;
//		// starting with y = x / 2 avoids magnitude issues with x squared
//		for (y = x.divide(two); y.compareTo(x.divide(y)) > 0; y = ((x.divide(y))
//				.add(y)).divide(two))
//			;
//		return y;
//	}
	
	/** Calculate the square root of a BigInteger in logarithmic time */
	public static BigInteger squareRoot(BigInteger x) { 
	      BigInteger right = x, left = BigInteger.ZERO, mid; 
	      while(right.subtract(left).compareTo(BigInteger.ONE) > 0) { 
	            mid = (right.add(left)).shiftRight(1);
	            if(mid.multiply(mid).compareTo(x) > 0) 
	                  right = mid; 
	            else 
	                  left = mid; 
	      } 
	      return left; 
	}

	private static BitSet genBinaryRow(BigInteger number,
			List<BigInteger> primes) {
		BitSet row = new BitSet(primes.size());
		for (int j = 0; j < primes.size(); j++) {
			BigInteger big = primes.get(j);
			while (number.mod(big).equals(BigInteger.ZERO)) {
				number = number.divide(big);
				row.set(j);
				;
			}
		}
		if (number.subtract(BigInteger.ONE).signum() == 0) {
			return row;
		} else {
			return null;
		}
	}

//	private static BitSet[] getMatrixColumns(List<BitSet> m,
//			List<BigInteger> primes) {
//		BitSet[] columns = new BitSet[primes.size()];
//		for (int j = 0; j < m.size(); j++) {
//			BitSet row = m.get(j);
//			for (int i = 0; i < primes.size(); i++) {
//				if (columns[i] == null) {
//					columns[i] = new BitSet(m.size());
//				}
//				if (row.get(i)) {
//					columns[i].set(j);
//				}
//			}
//		}
//		return columns;
//	}

	private static boolean trySolution(BitSet x, BitSet[] columns,
			BigInteger N, List<BigInteger> rs, int rows) {
		
			System.out.println("Trying a solution: " + x);
			BigInteger x2 = BigInteger.ONE;
			BigInteger y2 = BigInteger.ONE;
			for (int j = 0; j < rows; j++) {
				if (x.get(j)) {
					x2 = x2.multiply(rs.get(j));
					y2 = y2.multiply(rs.get(j)
							.modPow(BigInteger.valueOf(2L), N));
				}
			}
			x2 = x2.mod(N);
			y2 = squareRoot(y2).mod(N);
			// // System.out.println(x2);
			// // System.out.println(y2);
			BigInteger gcd = N.gcd(y2.subtract(x2));
			if (!gcd.equals(BigInteger.ONE) && !gcd.equals(N)) {
				BigInteger factor1 = gcd;
				BigInteger factor2 = N.divide(factor1);
				System.out.println("Factor 1: " + factor1);
				System.out.println("Factor 2: " + factor2);
				return true;
			}
			System.out.println("Did not find roots");
		return false;
	}

//	private static List<BitSet> copyMatrix(List<BitSet> matrix) {
//		List<BitSet> copy = new LinkedList<BitSet>();
//		for (BitSet set : matrix) {
//			BitSet bits = new BitSet(set.size());
//			bits.or(set);
//			copy.add(bits);
//		}
//		return copy;
//	}

//	private static int rowWithBitSet(List<BitSet> matrix, int index,
//			Set<Integer> removed) {
//		for (int i = 0; i < matrix.size(); i++) {
//			if (matrix.get(i).get(index) && !removed.contains(i)) {
//				return i;
//			}
//		}
//		return -1;
//	}
/*
	private static void findSolutions(List<BitSet> m, List<BigInteger> rs,
			List<BigInteger> primes, BigInteger N) {
		List<BitSet> solutions = new LinkedList<BitSet>();

		List<BitSet> matrix = copyMatrix(m);
		for (int i = 0; i < matrix.size(); i++) {
			BitSet set = new BitSet(matrix.size());
			set.set(i);
			solutions.add(set);
		}
		HashSet<Integer> removed = new HashSet<Integer>();
		for (int i = 0; i < primes.size(); i++) {
			int set = rowWithBitSet(matrix, i, removed);
			if (set == -1) {
				System.out.println("No row with bit: " + i + " set");
				continue;
			}
			System.out.println("Bit: " + i + " eliminated");
			// BitSet bits = matrix.remove(set);
			// solutions.remove(set);
			BitSet bits = matrix.get(set);
			removed.add(set);
			for (int j = 0; j < matrix.size(); j++) {
				if (removed.contains(j)) {
					continue;
				}
				BitSet row = matrix.get(j);
				if (row.get(i)) {
					row.xor(bits);
					solutions.get(j).set(set);
				}
			}
		}

		for (int i = 0; i < solutions.size(); i++) {
			if (removed.contains(i)) {
				continue;
			}
			BitSet x = solutions.get(i);
//			 System.out.println(x);
//			System.out.println(matrix.get(i));
			if (trySolution(x, getMatrixColumns(m, primes), N, rs, m.size())) {
				return;
			}
		}

	}
*/
	public static void createInputFile(LinkedList<BitSet> list, int M, int N) throws IOException{
		System.out.println("Generate Input File");
		try {
			File f = new File("HelpFunction/input.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write(M+" "+N+"\n");
			String s;
			for(int i = 0; i < M; i++){
				BitSet currentLine = list.get(i);
				for (int j = 0; j < N; j++){
					s = currentLine.get(j) ? "1":"0"; 
					writer.write(s+" ");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		System.out.println("Finish Generate Input File");	
		
	}
	public static ArrayList<BigInteger> readFile(String s){

		ArrayList<BigInteger> data = new ArrayList<BigInteger>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(s));
			
			int limit = Integer.parseInt(br.readLine());
			
			for(int i = 0; i < limit; i++){
				data.add(new BigInteger(br.readLine()));
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return data;
	}
	
	public static String findSolutions() throws IOException, InterruptedException{
		StringBuilder sb = new StringBuilder();
		Process p = Runtime.getRuntime().exec("g++ GaussBin.cpp",null,new File("./HelpFunction"));
		p.waitFor();
		String command = "./HelpFunction/a.out";
		ProcessBuilder pb = new ProcessBuilder(command,"./HelpFunction/input.txt","./HelpFunction/output.txt");
		Process process = pb.start();
		
		BufferedReader br =  new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + System.getProperty("line.separator"));
		}
		
		int errCode = process.waitFor();
		if (errCode != 0){
			System.out.println("Error from process "+ errCode);
		}

		return sb.toString();
	}
	public static void main(String[] args) throws IOException, InterruptedException {
		ArrayList<BigInteger> numbers = readFile("Data/input.txt");
		BigInteger N = numbers.get(3);
		int diff = 10;
		int l = 1000;
		List<BigInteger> primes = genPrimes(l - diff);

		
		int block = 10;
		int factor = 20;
		int jump = 1;
		LinkedList<BitSet> m = new LinkedList<BitSet>();
		List<BigInteger> rs = genRs(l, N, primes, m, block, factor, jump);
		System.out.println("R:s generated...");
		
		createInputFile(m,m.size(),primes.size());
		String rejected = findSolutions();
		System.out.println(rejected);
		//findSolutions(m, rs, primes, N);
		

	}

}
