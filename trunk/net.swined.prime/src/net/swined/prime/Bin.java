package net.swined.prime;

import java.util.HashMap;

public class Bin {

	public static IExpression not(IExpression e) {
		if (e instanceof Const)
			return ((Const)e).not();
		if (e instanceof Not)
			return ((Not)e).e;
		return new Not(e);
	}
	
  public static IExpression and(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).and(b);
    if (b instanceof Const)
      return ((Const) b).and(a);
    if (a instanceof Var && b instanceof Var) {
      Var va = (Var)a;
      Var vb = (Var)b;
      if (va.name == vb.name)
        return a;
    }
    return new And(a, b);
  }

  public static IExpression or(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).or(b);
    if (b instanceof Const)
      return ((Const) b).or(a);
    if (a instanceof Var && b instanceof Var) {
      Var va = (Var)a;
      Var vb = (Var)b;
      if (va.name == vb.name)
        return a;
    }
    return new Or(a, b);
  }

  public static IExpression xor(IExpression a, IExpression b) {
	    if (a instanceof Const)
	        return ((Const) a).xor(b);
	      if (b instanceof Const)
	        return ((Const) b).xor(a);
	      if (a instanceof Var && b instanceof Var) {
	          Var va = (Var)a;
	          Var vb = (Var)b;
	          if (va.name == vb.name)
	            return Const.ZERO;
	        }
    return new Xor(a, b);
  }
	
  public static IExpression m2(IExpression a, IExpression b, IExpression c) {
    return or(and(a, b), or(and(b, c), and(a, c)));
  }  

  public static IExpression xor(IExpression a, IExpression b, IExpression c) {
	  return xor(xor(a, b), c);
  }  
 
  public static IExpression sub(IExpression x, int v, IExpression s) {
	  if (!x.hasVar(v))
		  return x;
	  IExpression a = x.sub(v, Const.ONE, new HashMap<IExpression, IExpression>());
	  IExpression b = x.sub(v, Const.ZERO, new HashMap<IExpression, IExpression>());
	  return or(and(s, a), and(not(s), b));
  }
  
}
