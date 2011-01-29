import java.math.BigInteger;
import java.util.Random;

import net.swined.prime.binary.Const;
import net.swined.prime.binary.IExpression;
import net.swined.prime.binary.Int;

import org.junit.Assert;
import org.junit.Test;

public class IntTest {

	private static Random random = new Random();
	
	private static BigInteger genInt() {
		return BigInteger.valueOf(random.nextInt()).abs().mod(BigInteger.valueOf(1000));
	}
	
	@Test
	public void testGe() {
		for (int i = 0; i < 10000; i++) {
			BigInteger a = genInt(); 
			BigInteger b = genInt();
			IExpression[] x = Int.pad(Int.toExp(a), 10);
			IExpression[] y = Int.pad(Int.toExp(b), 10);
			Const ge = (Const)Int.ge(x, y);
			Const p = a.compareTo(b) >= 0 ? Const.ONE : Const.ZERO;
			Assert.assertEquals("" + a + " vs " + b, p, ge);
		}
	}
	
}
