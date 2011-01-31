package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.HashMap;

public class Bin {

	public static IExpression split(IExpression e) {
		BigInteger vars = e.getVars();
		for (int i = 0; i < vars.bitLength(); i++)
			if (vars.testBit(i)) {
				Var v = new Var(i);
        IExpression p = and(v, e.sub(i, Const.ONE, new HashMap<IExpression, IExpression>()));
				IExpression n = and(not(v), e.sub(i, Const.ZERO, new HashMap<IExpression, IExpression>()));
				e = or(p, n);
			}
		return e;
	}

  public static IExpression and(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).and(b);
    if (b instanceof Const)
      return ((Const) b).and(a);
    if (a instanceof Not && b instanceof Not)
      return not(or(not(a), not(b)));
    return new BinOp(BinOpType.AND, a, b);
  }

  public static IExpression or(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).or(b);
    if (b instanceof Const)
      return ((Const) b).or(a);
    if (a instanceof Not && b instanceof Not)
      return not(and(not(a), not(b)));
    return new BinOp(BinOpType.OR, a, b);
  }
  
  public static IExpression not(IExpression a) {
    if (a instanceof Const)
      return ((Const) a).not();
    if (a instanceof Not)
      return ((Not) a).a;
    return new Not(a);
  }
  
  public static IExpression xor(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).xor(b);
    if (b instanceof Const)
      return ((Const) b).xor(a);
    if (a instanceof Not && b instanceof Not)
      return xor(not(a), not(b));
    return new BinOp(BinOpType.XOR, a, b);
  }
	
}
