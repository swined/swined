package org.swined.dsa;

import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.util.Map;



public class BinOp implements IExpression {

  private final BinOpType type;
  private final IExpression a;
  private final IExpression b;
  private SoftReference<BigInteger> vars;

  public BinOp(BinOpType type, IExpression a, IExpression b) {
    type.checkConstraints(a, b);
    this.type = type;
    this.a = a;
    this.b = b;
  }

  @Override
  public String toString() {
      return "(" + a + " " + type.sign + " " + b + ")";
  }

  @Override
  public BigInteger getVars() {
    BigInteger v = vars == null ? null : vars.get();
    if (v == null) {
      v = a.getVars().or(b.getVars());
      vars = new SoftReference<BigInteger>(v);
    }
    return v;
  }
    
	@Override
	public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
	  if (!getVars().testBit(v))
	    return this;
	  IExpression s = ctx.get(this);
	  if (s == null) {
  		IExpression sa = a.sub(v, c, ctx);
      IExpression sb = b.sub(v, c, ctx);
      if (sa != a || sb != b)
        s = type.apply(sa, sb);
      else
        s = this;
      ctx.put(this, s);
	  }
	  return s;
	}
	
}
