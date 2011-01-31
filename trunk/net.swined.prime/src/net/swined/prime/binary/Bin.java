package net.swined.prime.binary;

import java.math.BigInteger;
import net.swined.prime.nodes.*;

public class Bin {

	public static IExpression split(IExpression e) {
		BigInteger vars = e.getVars();
		for (int i = 0; i < vars.bitLength(); i++)
			if (vars.testBit(i)) {
				IExpression p = and(new Var(i), e.sub(i, Const.ONE));
				IExpression n = and(not(new Var(i)), e.sub(i, Const.ZERO));
				e = or(p, n);
			}
		return e;
	}

  public static IExpression and(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).and(b);
    if (b instanceof Const)
      return ((Const) b).and(a);
    return new And(a, b);
  }

  public static IExpression or(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).or(b);
    if (b instanceof Const)
      return ((Const) b).or(a);
    return new Or(a, b);
  }
  
  public static IExpression not(IExpression a) {
    if (a instanceof Const)
      return ((Const) a).not();
    return new Not(a);
  }
  
  public static IExpression xor(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).xor(b);
    if (b instanceof Const)
      return ((Const) b).xor(a);
    return new Xor(a, b);
  }
	
}
