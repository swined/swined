package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.HashMap;

public class Bin {

	public static IExpression split(IExpression e) {
		BigInteger vars = e.getVars();
		for (int i = 0; i < vars.bitLength(); i++)
			if (vars.testBit(i)) {
				Var v = new Var(i, false);
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
    if (a instanceof Var && b instanceof Var) {
      Var va = (Var)a;
      Var vb = (Var)b;
      if (va.name == vb.name) {
        return va.sign == vb.sign ? a : Const.ZERO;
      }
      if (va.sign && vb.sign)
        return not(or(not(a), not(b)));
    }
    return new BinOp(BinOpType.AND, a, b);
  }

  public static IExpression or(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).or(b);
    if (b instanceof Const)
      return ((Const) b).or(a);
    if (a instanceof Not && b instanceof Not)
      return not(and(not(a), not(b)));
    if (a instanceof Var && b instanceof Var) {
      Var va = (Var)a;
      Var vb = (Var)b;
      if (va.name == vb.name) {
        return va.sign == vb.sign ? a : Const.ONE;
      }
      if (va.sign && vb.sign)
        return not(and(not(a), not(b)));
    }
    return new BinOp(BinOpType.OR, a, b);
  }
  
  public static IExpression not(IExpression a) {
    if (a instanceof Const)
      return ((Const) a).not();
    if (a instanceof Not)
      return ((Not) a).a;
    if (a instanceof Var)
      return ((Var) a).not;
    return new Not(a);
  }
  
  public static IExpression xor(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).xor(b);
    if (b instanceof Const)
      return ((Const) b).xor(a);
    if (a instanceof Not && b instanceof Not)
      return xor(not(a), not(b));
    if (a instanceof Var && b instanceof Var) {
      Var va = (Var)a;
      Var vb = (Var)b;
      if (va.name == vb.name) {
        return va.sign == vb.sign ? Const.ZERO : Const.ONE;
      }
      if (va.sign && vb.sign)
        return xor(not(va), not(vb));
    }
    return new BinOp(BinOpType.XOR, a, b);
  }
	
  public static IExpression ge(IExpression a, IExpression b, IExpression g) {
    // a & !b | (a ^ !b) & g
    if (a == Const.ZERO) // !b & g
      return and(not(b), g);
    if (a == Const.ONE) // !b | b & g
      return or(not(b), and(b, g));
    if (b == Const.ZERO) // a | !a & g
      return or(a, and(not(b), g));
    if (b == Const.ONE) //a & g
      return and(a, g);
    return new TerOp(TerOpType.GE, a, b, g);
  }

  public static IExpression m2(IExpression a, IExpression b, IExpression c) {
    if (a instanceof Const)
      return ((Const) a).m2(b, c);
    if (b instanceof Const)
      return ((Const) b).m2(a, c);
    if (c instanceof Const)
      return ((Const) c).m2(b, a);
    return new TerOp(TerOpType.M2, a, b, c);
  }  

  public static IExpression xor(IExpression a, IExpression b, IExpression c) {
    return Bin.xor(Bin.xor(a, b), c);
  }  
  
}
