package project1;

import java.math.BigInteger;
import java.util.BitSet;

public class BitSetTest {

	public static void main(String[] args) {
		BitSet bits = new BitSet(1000);
		System.out.println(bits.length());
		System.out.println(bits.size());
		BitSet bits2 = new BitSet(1000);
		bits2.set(4);
		bits.set(4);
		System.out.println(bits);
		System.out.println(bits2);
		bits.xor(bits2);
		System.out.println(bits);
		System.out.println(new BigInteger("354864246643").multiply(new BigInteger("276678055057")).compareTo(new BigInteger("98183149570452781423651")));

	}

}
