package project1;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Primes {
	
	private static BigInteger[] genPrimes(int nbrOfPrimes){
		  BigInteger[] primes = new BigInteger[nbrOfPrimes];
		  int i = 0;
		  BigInteger prime = BigInteger.ONE;
		  boolean found;
		  while(i < nbrOfPrimes){
		    found = false;
		    for (int j = 1; j < i ; j++){
//		    	System.out.println("i: " + i + " j: " + j);
		       if (prime.mod(primes[j]).equals(BigInteger.ZERO)){
		         found = true;
		         break;
		       }
		    }
		    if (!found){
		      primes[i] = prime;
//		      System.out.println(primes[i]);
		      i++;
		    }
		    prime = prime.add(BigInteger.ONE);
		  }
		  return primes;
	}
	
	private static List<BigInteger> genPrimesLessThan(int upper){
		LinkedList<BigInteger> primes = new LinkedList<BigInteger>();
		LinkedList<Integer> sieve = new LinkedList<Integer>();
		for(int i = 1; i <= upper; i++){
			sieve.add(i);
		}
		//primes.add(new BigInteger(sieve.removeFirst().toString()));
		sieve.removeFirst();
		while(!sieve.isEmpty()){
			int e = sieve.removeFirst();
			primes.add(new BigInteger(Integer.toString(e)));
			int i = 2;
			while(i*e <= upper){
				int index = sieve.indexOf(i*e);
				if (index >= 0){
					sieve.remove(index);
				}
				i++;
			}
		}
		return primes;
	}

	private static boolean bSmooth(BigInteger number, BigInteger[] primes){
		  for (int i = 1; i < primes.length; ++i){
			    while(number.mod(primes[i]).equals(BigInteger.ZERO)){
//			      System.out.printf("%d*",primes[i]);
			      number = number.divide(primes[i]);
			    }
			  }
//			  System.out.printf("%d\n",number);
			  return number.subtract(BigInteger.ONE).signum() == 0;
	}
	
	private static boolean bSmooth(BigInteger number, List<BigInteger> primes){
		  for (BigInteger big : primes){
			    while(number.mod(big).equals(BigInteger.ZERO)){
//			      System.out.print(big + "*");
			      number = number.divide(big);
			    }
			  }
//			  System.out.println(number);
			  return number.subtract(BigInteger.ONE).signum() == 0;
	}
	
	private static List<BigInteger> genRs(int bValue, int lValue, BigInteger nValue){
		LinkedList<BigInteger> rs = new LinkedList<BigInteger>();
		List<BigInteger> primes = genPrimesLessThan(bValue);
		int jump = 0;
		while(rs.size() < lValue){
			for (int k = 1; k <= 10; k++){
				for (int j = 1; j <= 10; j++){
					BigInteger ki = new BigInteger(Integer.toString(k+jump));
					BigInteger ji = new BigInteger(Integer.toString(j+jump));
					BigInteger r = bigIntSqRootFloor(ki.multiply(nValue)).add(ji);
					BigInteger r2 = r.modPow(BigInteger.valueOf(2L),nValue);
					if (bSmooth(r2,primes)){
						System.out.println("k: " + ki + " j: " + ji + " r: " + r + " r2: " + r2);
						rs.add(r);
					}
				}
			}
			jump += 10;
		}
		return rs;
	}
	
	public static void main(String[] args) {
//		for(BigInteger big: genPrimes(1000)){
//			System.out.println(big);
//		}
		//System.out.println(bSmooth(new BigInteger("264"),genPrimes(12)));
		//System.out.println(genPrimesLessThan(1000));
		//System.out.println(bSmooth(new BigInteger("714"),genPrimesLessThan(30)));
		genRs(30,12,BigInteger.valueOf(16637L));
	}
	
	public static BigInteger bigIntSqRootFloor(BigInteger x)
	        throws IllegalArgumentException {
	    if (x.compareTo(BigInteger.ZERO) < 0) {
	        throw new IllegalArgumentException("Negative argument.");
	    }
	    // square roots of 0 and 1 are trivial and
	    // y == 0 will cause a divide-by-zero exception
	    if (x .equals(BigInteger.ZERO) || x.equals(BigInteger.ONE)) {
	        return x;
	    } // end if
	    BigInteger two = BigInteger.valueOf(2L);
	    BigInteger y;
	    // starting with y = x / 2 avoids magnitude issues with x squared
	    for (y = x.divide(two);
	            y.compareTo(x.divide(y)) > 0;
	            y = ((x.divide(y)).add(y)).divide(two));
	    return y;
	}

}
