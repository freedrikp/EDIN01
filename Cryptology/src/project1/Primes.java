package project1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Primes {

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

	private static List<BigInteger> genPrimesLessThan(int upper) {
		LinkedList<BigInteger> primes = new LinkedList<BigInteger>();
		LinkedList<Integer> sieve = new LinkedList<Integer>();
		for (int i = 1; i <= upper; i++) {
			sieve.add(i);
		}
		sieve.removeFirst();
		while (!sieve.isEmpty()) {
			int e = sieve.removeFirst();
			primes.add(new BigInteger(Integer.toString(e)));
			int i = 2;
			while (i * e <= upper) {
				int index = sieve.indexOf(i * e);
				if (index >= 0) {
					sieve.remove(index);
				}
				i++;
			}
		}
		return primes;
	}

	private static List<BigInteger> genRs(int lValue, BigInteger nValue,
			List<BigInteger> primes, List<String> m) {
		int block = 20;
		LinkedList<BigInteger> rs = new LinkedList<BigInteger>();
		int jump = 0;
		int k = 1;
		while (rs.size() < lValue) {
			BigInteger ki = new BigInteger(Integer.toString(k));
			int start = k - block - (k / 10);
			int stop = k + block + (k / 10);
			if (start < 1) {
				start = 1;
			}
			for (int j = start; j <= stop; j++) {
				BigInteger ji = new BigInteger(Integer.toString(j + jump));
				BigInteger r = bigIntSqRootFloor(ki.multiply(nValue)).add(ji);
				BigInteger r2 = r.pow(2).mod(nValue);
				String binrow = genBinaryRow(r2, primes);
				if (binrow != null && !m.contains(binrow)) {
					m.add(binrow);
					rs.add(r);
					// System.out.println("Added: " + rs.size() + " K: " + ki
					// + " J: " + ji);
					if (rs.size() >= lValue) {
						return rs;
					}
				}
			}
			k++;
		}
		return rs;
	}

	public static BigInteger bigIntSqRootFloor(BigInteger x)
			throws IllegalArgumentException {
		if (x.compareTo(BigInteger.ZERO) < 0) {
			throw new IllegalArgumentException("Negative argument.");
		}
		// square roots of 0 and 1 are trivial and
		// y == 0 will cause a divide-by-zero exception
		if (x.equals(BigInteger.ZERO) || x.equals(BigInteger.ONE)) {
			return x;
		} // end if
		BigInteger two = BigInteger.valueOf(2L);
		BigInteger y;
		// starting with y = x / 2 avoids magnitude issues with x squared
		for (y = x.divide(two); y.compareTo(x.divide(y)) > 0; y = ((x.divide(y))
				.add(y)).divide(two))
			;
		return y;
	}

	private static String genBinaryRow(BigInteger number,
			List<BigInteger> primes) {
		String row = "";
		for (BigInteger big : primes) {
			int i = 0;
			while (number.mod(big).equals(BigInteger.ZERO)) {
				i++;
				number = number.divide(big);
			}
			row += Integer.toString(i % 2);
		}
		if (number.subtract(BigInteger.ONE).signum() == 0) {
			return row;
		} else {
			return null;
		}
	}

	private static String multiply(String row, String column) {
		int result = 0;
		for (int i = 0; i < row.length(); i++) {
			int r = row.charAt(i) - '0';
			int c = column.charAt(i) - '0';
			result += r * c;
		}
		return Integer.toString(result % 2);
	}

	private static boolean isZero(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != '0') {
				return false;
			}
		}
		return true;
	}

	private static String[] getMatrixColumns(List<String> m,
			List<BigInteger> primes) {
		String[] columns = new String[primes.size()];
		for (String row : m) {
			for (int i = 0; i < row.length(); i++) {
				if (columns[i] == null) {
					columns[i] = "";
				}
				columns[i] += row.charAt(i);
			}
		}
		return columns;
	}

	private static String bigIntToBinaryString(BigInteger i, int digits) {
		String x = "";
		for (int j = 0; j < i.bitLength(); j++) {
			if (i.testBit(i.bitLength() - 1 - j)) {
				x += "1";
			} else {
				x += "0";
			}
		}
		for (int j = x.length(); j < digits; j++) {
			x = "0" + x;
		}
		return x;
	}

	private static boolean trySolution(String x, String[] columns,
			BigInteger N, List<BigInteger> rs) {
		String result = "";
		for (String col : columns) {
			result += multiply(x, col);
		}
//		System.out.println(x);
		if (isZero(result)) {
			System.out.println("Trying a solution: " + x);
			BigInteger x2 = BigInteger.ONE;
			BigInteger y2 = BigInteger.ONE;
			for (int j = 0; j < x.length(); j++) {
				if (x.charAt(j) == '1') {
					x2 = x2.multiply(rs.get(j));
					y2 = y2.multiply(rs.get(j)
							.modPow(BigInteger.valueOf(2L), N));
				}
			}
			x2 = x2.mod(N);
			y2 = bigIntSqRootFloor(y2).mod(N);
			// System.out.println(x2);
			// System.out.println(y2);
			BigInteger gcd = N.gcd(y2.subtract(x2));
			if (!gcd.equals(BigInteger.ONE) && !gcd.equals(N)) {
				BigInteger factor1 = gcd;
				BigInteger factor2 = N.divide(factor1);
				System.out.println("Factor 1: " + factor1);
				System.out.println("Factor 2: " + factor2);
				return true;
			}
			System.out.println("Did not find roots");
		}
		return false;
	}

	private static void findSolutions(String[] columns, List<BigInteger> rs,
			List<BigInteger> primes, BigInteger N) {
		for (BigInteger i = BigInteger.ZERO; BigInteger.ONE
				.shiftLeft(primes.size()).subtract(i).signum() == 1; i = i
				.add(BigInteger.ONE)) {
//			 Random rand = new Random();
//			 while (true) {
//			 BigInteger i = new BigInteger(primes.size(), rand);
			String x = bigIntToBinaryString(i, primes.size());
			if (!isZero(x)) {
				if (trySolution(x, columns, N, rs)) {
					return;
				}
			}
		}
//		int  threads = 4;
//		BigInteger lower = BigInteger.ONE;
//		BigInteger size = BigInteger.ONE.shiftLeft(primes.size()).divide(BigInteger.ONE.shiftLeft(2));
//		BigInteger upper = size;
//		Thread[] solvers = new Solver[threads];
//		for(int i = 0; i < threads; i++){
//			solvers[i] = new Solver(lower, upper, columns, N, rs, primes, solvers);
//			lower = upper;
//			if (i < threads -1){
//				upper = upper.add(size);				
//			}else{
//				upper = BigInteger.ONE.shiftLeft(primes.size());
//			}
//		}
//		for(Thread t: solvers){
//			t.start();
//		}
	}

//	private static class Solver extends Thread {
//		private BigInteger lower;
//		private BigInteger upper;
//		private String[] columns;
//		private BigInteger N;
//		private List<BigInteger> rs;
//		private List<BigInteger> primes;
//		private Thread[] solvers;
//
//		public Solver(BigInteger lower, BigInteger upper, String[] columns, BigInteger N,
//				List<BigInteger> rs, List<BigInteger> primes, Thread[] solvers) {
//			super();
//			this.lower = lower;
//			this.upper = upper;
//			this.columns = columns;
//			this.N = N;
//			this.rs = rs;
//			this.primes = primes;
//			this.solvers = solvers;
//		}
//
//		public void run() {
//			for (BigInteger i = lower; upper.subtract(i).signum() == 1
//					&& !isInterrupted(); i = i.add(BigInteger.ONE)) {
//				// Random rand = new Random();
//				// while (true) {
//				// BigInteger i = new BigInteger(primes.size(), rand);
//				String x = bigIntToBinaryString(i, primes.size());
//				if (!isZero(x)) {
//					if (trySolution(x, columns, N, rs)) {
//						for(Thread t: solvers){
//							t.interrupt();
//						}
//					}
//				}
//			}
//
//		}
//	}

	public static void main(String[] args) {
		 BigInteger N = new BigInteger("98183149570452781423651");
		// BigInteger N = new BigInteger("392742364277");
		// BigInteger N = new BigInteger("3205837387");
//		 BigInteger N = new BigInteger("31741649");
		// BigInteger N = new BigInteger("307561");
		// BigInteger N = new BigInteger("323");
//		BigInteger N = new BigInteger("16637");
		int diff = 5;
		int l = 1000;
		List<BigInteger> primes = genPrimes(l-diff);
		// List<BigInteger> primes = genPrimesLessThan(100);
		System.out.println("Primes generated...");
		List<String> m = new LinkedList<String>();
		List<BigInteger> rs = genRs(l, N, primes, m);
		System.out.println("R:s generated...");
		String[] columns = getMatrixColumns(m, primes);
		findSolutions(columns, rs, primes, N);

	}

}
