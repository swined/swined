import java.math.BigInteger;
import java.util.Random;

import net.swined.prime.binary.Int;

import org.junit.Assert;
import org.junit.Test;
import net.swined.prime.binary.*;
public class IntTest {

	private static Random random = new Random();
	
	private static BigInteger genInt(int len) {
		return BigInteger.valueOf(random.nextInt()).abs().mod(BigInteger.ZERO.setBit(len).subtract(BigInteger.ONE)).add(BigInteger.ONE);
	}
	
	@Test
	public void testGe() {
		for (int i = 0; i < 10000; i++) {
			BigInteger a = genInt(10); 
			BigInteger b = genInt(10);
			Const ge = (Const)Int.ge(Int.pad(Int.toExp(a), 10), Int.pad(Int.toExp(b), 10));
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
	public void testMod() {
		for (int i = 0; i < 10000; i++) {
			BigInteger a = genInt(10); 
			BigInteger b = genInt(4);
			BigInteger r = Int.toInt(Int.mod(Int.toExp(a), Int.toExp(b))); 
			Assert.assertEquals("" + a + " mod " + b, a.mod(b), r);
		}
	}
	
}
