package net.swined.prime;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Bin {

	public static IExpression split(IExpression e) {
		BigInteger vars = e.getVars();
		for (int i = 0; i < vars.bitLength(); i++)
			if (vars.testBit(i)) {
				Var v = new Var(i, false);
        IExpression p = and(v, e.sub(i, Const.ONE, new HashMap<IExpression, IExpression>()));
				IExpression n = and(v.not(), e.sub(i, Const.ZERO, new HashMap<IExpression, IExpression>()));
				e = or(p, n);
			}
		return e;
	}

  public static IExpression and(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).and(b);
    if (b instanceof Const)
      return ((Const) b).and(a);
    if (a instanceof Var && b instanceof Var) {
      Var va = (Var)a;
      Var vb = (Var)b;
      if (va.name == vb.name) {
        return va.sign == vb.sign ? a : Const.ZERO;
      }
    }
    return new BinOp(BinOpType.AND, a, b);
  }

  public static IExpression or(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).or(b);
    if (b instanceof Const)
      return ((Const) b).or(a);
    if (a instanceof Var && b instanceof Var) {
      Var va = (Var)a;
      Var vb = (Var)b;
      if (va.name == vb.name) {
        return va.sign == vb.sign ? a : Const.ONE;
      }
    }
    return new BinOp(BinOpType.OR, a, b);
  }

  public static Set<Map<Integer, Const>> solve(IExpression e) {
    Set<Map<Integer, Const>> r = new HashSet<Map<Integer, Const>>();
    if (e instanceof Const) {
      if (e == Const.ONE)
        r.add(new HashMap<Integer, Const>());
      return r;
    }
    int v = e.getVars().getLowestSetBit();
    
    for (Const c : Const.values()) {
      Set<Map<Integer, Const>> s = solve(e.sub(v, c, new HashMap<IExpression, IExpression>()));
      if (s == null)
        continue;
      for (Map<Integer, Const> p : s) {
        p.put(v, c);
        r.add(p);
      }
    }
    return r;
  }
  
  public static IExpression xor(IExpression a, IExpression b) {
    return or(and(a, b.not()), and(a.not(), b));
  }
	
  public static IExpression ge(IExpression a, IExpression b, IExpression g) {
    return or(and(a, b.not()), and(xor(a, b.not()), g)); 
  }

  public static IExpression m2(IExpression a, IExpression b, IExpression c) {
    return or(and(a, b), or(and(b, c), and(a, c)));
  }  

  public static IExpression xor(IExpression a, IExpression b, IExpression c) {
	  return xor(xor(a, b), c);
  }  
  
}
