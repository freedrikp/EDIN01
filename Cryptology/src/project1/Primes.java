package project1;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.NVList;

public class Primes {

	private static BigInteger[] genPrimes(int nbrOfPrimes) {
		BigInteger[] primes = new BigInteger[nbrOfPrimes];
		int i = 0;
		BigInteger prime = BigInteger.ONE;
		boolean found;
		while (i < nbrOfPrimes) {
			found = false;
			for (int j = 1; j < i; j++) {
				// System.out.println("i: " + i + " j: " + j);
				if (prime.mod(primes[j]).equals(BigInteger.ZERO)) {
					found = true;
					break;
				}
			}
			if (!found) {
				primes[i] = prime;
				// System.out.println(primes[i]);
				i++;
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
		// primes.add(new BigInteger(sieve.removeFirst().toString()));
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

	private static boolean bSmooth(BigInteger number, BigInteger[] primes) {
		for (int i = 1; i < primes.length; ++i) {
			while (number.mod(primes[i]).equals(BigInteger.ZERO)) {
				// System.out.printf("%d*",primes[i]);
				number = number.divide(primes[i]);
			}
		}
		// System.out.printf("%d\n",number);
		return number.subtract(BigInteger.ONE).signum() == 0;
	}

	private static boolean bSmooth(BigInteger number, List<BigInteger> primes) {
		for (BigInteger big : primes) {
			while (number.mod(big).equals(BigInteger.ZERO)) {
				// System.out.print(big + "*");
				number = number.divide(big);
			}
		}
		// System.out.println(number);
		return number.subtract(BigInteger.ONE).signum() == 0;
	}

	private static List<BigInteger> genRs(int lValue,
			BigInteger nValue,List<BigInteger> primes, List<String> m) {
		LinkedList<BigInteger> rs = new LinkedList<BigInteger>();
		int jump = 0;
		while (rs.size() < lValue) {
			for (int k = 1; k <= 10; k++) {
				for (int j = 1; j <= 10; j++) {
					BigInteger ki = new BigInteger(Integer.toString(k + jump));
					BigInteger ji = new BigInteger(Integer.toString(j + jump));
					BigInteger r = bigIntSqRootFloor(ki.multiply(nValue)).add(
							ji);
					BigInteger r2 = r.modPow(BigInteger.valueOf(2L), nValue);
					if (bSmooth(r2, primes)) {
						//System.out.println("k: " + ki + " j: " + ji + " r: "
							//	+ r + " r2: " + r2);
						//System.out.println(genBinaryRow(r2, primes));
						String binrow = genBinaryRow(r2,primes);
						if (!m.contains(binrow)){
							m.add(binrow);
							rs.add(r);							
						}
					}
				}
			}
			jump += 10;
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

	private static String genBinaryRow(BigInteger number, List<BigInteger> primes) {
		String row = "";
		  for (BigInteger big : primes){
			  int i = 0;
			    while(number.mod(big).equals(BigInteger.ZERO)){
			    	i++;
			      number = number.divide(big);
			    }
			    row += Integer.toString(i % 2);
			 }
		  return row;
	}
	
	private static String multiply(String row, String column){
		int result = 0;
		for (int i = 0; i < row.length(); i++){
			int r = row.charAt(i) - '0';
			int c = column.charAt(i) -'0';
			result += r*c;
		}
		return Integer.toString(result % 2);
	}
	
	private static boolean isZero(String s){
		for (int i = 0; i < s.length(); i++){
			if (s.charAt(i) != '0'){
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		// for(BigInteger big: genPrimes(1000)){
		// System.out.println(big);
		// }
		// System.out.println(bSmooth(new BigInteger("264"),genPrimes(12)));
		// System.out.println(genPrimesLessThan(1000));
		// System.out.println(bSmooth(new
		// BigInteger("714"),genPrimesLessThan(30)));
		BigInteger N = new BigInteger("98183149570452781423651");
		List<BigInteger> primes = genPrimesLessThan(500);
		List<String> m = new LinkedList<String>();
		List<BigInteger> rs = genRs(1000,N ,primes,m);
		String[] columns = new String[primes.size()];
		for (String row: m){
			for (int i = 0; i < row.length(); i ++){
				if (columns[i] == null){
					columns[i] = "";
				}
				columns[i]+= row.charAt(i);
			}
		}
//		System.out.println(m);
//		for (String col : columns){
//			System.out.println(col);
//		}
		for (BigInteger i = BigInteger.ZERO; BigInteger.ONE.shiftLeft(primes.size()).subtract(i).signum() == 1; i = i.add(BigInteger.ONE)){
			String x = "";
			for ( int j = 0; j < i.bitLength(); j++){
				if ( i.testBit(i.bitLength()-1-j)){
					x += "1";
				}else{
					x += "0";
				}
			}
			for (int j = x.length(); j < primes.size(); j++){
				x = "0" + x;
			}
			String result = "";
			for (String col : columns){
				result += multiply(x,col);
			}
			if (isZero(result)){
				BigInteger x2 = BigInteger.ONE;
				BigInteger y2 = BigInteger.ONE;
				for (int j = 0; j < x.length(); j++){
					if (x.charAt(j) == '1'){
						x2 = x2.multiply(rs.get(j));
						y2 = y2.multiply(rs.get(j).modPow(BigInteger.valueOf(2L),N));
					}
				}
				x2 = x2.mod(N);
				y2 = bigIntSqRootFloor(y2).mod(N);
//				System.out.println(x2);
//				System.out.println(y2);
				BigInteger gcd = N.gcd(y2.subtract(x2));
				if (!gcd.equals(BigInteger.ONE) && !gcd.equals(N)){
					BigInteger factor1 = gcd;
					BigInteger factor2 = N.divide(factor1);
					System.out.println("Factor 1: " + factor1);
					System.out.println("Factor 2: " + factor2);
					break;
				}
			}
		}
	}

}
