import java.math.BigInteger;
import java.util.Random;

import net.swined.prime.binary.Const;
import net.swined.prime.binary.IExpression;
import net.swined.prime.binary.Int;

import org.junit.Assert;
import org.junit.Test;

public class IntTest {

	@Test
	public void testGe() {
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			BigInteger a = BigInteger.valueOf(random.nextInt()).abs();
			BigInteger b = BigInteger.valueOf(random.nextInt()).abs();
			IExpression[] x = Int.toExp(a);
			IExpression[] y = Int.toExp(b);
			if (x.length < y.length)
				x = Int.pad(x, y.length);
			if (y.length < x.length)
				y = Int.pad(y, x.length);
			Const ge = (Const)Int.ge(x, y);
			Const p = a.compareTo(b) >= 0 ? Const.ONE : Const.ZERO;
			Assert.assertEquals("" + a + " vs " + b, ge, p);
		}
	}
	
}
