package project1;

import java.math.BigInteger;

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
	
	public static void main(String[] args) {
//		for(BigInteger big: genPrimes(1000)){
//			System.out.println(big);
//		}
		System.out.println(bSmooth(new BigInteger("264"),genPrimes(12)));

	}

}
