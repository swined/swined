package net.swined.prime.binary;

import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.util.Map;



public class TerOp implements IExpression {

  private final TerOpType type;
  private final IExpression a;
  private final IExpression b;
  private final IExpression c;
  private SoftReference<BigInteger> vars;

  public TerOp(TerOpType type, IExpression a, IExpression b, IExpression c) {
    type.checkConstraints(a, b, c);
    this.type = type;
    this.a = a;
    this.b = b;
    this.c = c;
  }

  @Override
  public String toString() {
      return type.sign + "(" + a + ", " + b + ", " + c + ")";
  }

  @Override
  public BigInteger getVars() {
    BigInteger v = vars == null ? null : vars.get();
    if (v == null) {
      v = a.getVars().or(b.getVars()).or(c.getVars());
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
      IExpression sc = c.sub(v, c, ctx);
      if (sa != a || sb != b || sc != c)
        s = type.apply(sa, sb, sc);
      else
        s = this;
      ctx.put(this, s);
	  }
	  return s;
	}
	
}
