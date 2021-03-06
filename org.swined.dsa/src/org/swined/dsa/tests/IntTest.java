package org.swined.dsa.tests;
import java.math.BigInteger;
import java.util.Random;


import org.junit.Assert;
import org.junit.Test;
import org.swined.dsa.*;
public class IntTest {

	private static Random random = new Random();
	
	private static BigInteger genInt(int len) {
		return BigInteger.valueOf(random.nextInt()).abs().mod(BigInteger.ZERO.setBit(len).subtract(BigInteger.ONE)).add(BigInteger.ONE);
	}
	
	@Test
	public void testGeN() {
		for (int i = 0; i < 10000; i++) {
			BigInteger a = genInt(10); 
			BigInteger b = genInt(10);
			Const ge = (Const)Int.ge(Int.pad(Int.toExp(a), 10), b);
			Const p = a.compareTo(b) >= 0 ? Const.ONE : Const.ZERO;
			Assert.assertEquals("" + a + " vs " + b, p, ge);
		}
	}
	
	@Test
	public void testSum() {
		for (int i = 0; i < 10000; i++) {
			BigInteger a = genInt(10); 
			BigInteger b = genInt(10);
			BigInteger r = Int.toInt(Int.sum(Int.pad(Int.toExp(a), 10), Int.pad(Int.toExp(b), 10)));
			Assert.assertEquals("" + a + " + " + b, a.add(b).mod(BigInteger.ZERO.setBit(10)), r);
		}
	}

	@Test
	public void testNegate() {
		for (int i = 0; i < 10000; i++) {
			BigInteger a = genInt(10); 
			BigInteger r = Int.toInt(Int.negate(Int.negate(Int.toExp(a))));
			Assert.assertEquals(a, r);
		}
	}
	
	@Test
	public void testModN() {
		for (int i = 0; i < 10000; i++) {
			BigInteger a = genInt(10); 
			BigInteger b = genInt(4);
			BigInteger r = Int.toInt(Int.mod(Int.toExp(a), b)); 
			Assert.assertEquals("" + a + " mod " + b, a.mod(b), r);
		}
	}
	
	@Test
	public void testModPow() {
		for (int i = 0; i < 10000; i++) {
			BigInteger a = genInt(4); 
			BigInteger b = genInt(4);
			BigInteger c = genInt(4);
			BigInteger r = Int.toInt(Int.modPow(a, Int.toExp(b), c).getBits()); 
			Assert.assertEquals("" + a + " ^ " + b + " mod " + c, a.modPow(b, c), r);
		}
	}
	
}
