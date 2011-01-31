package net.swined.prime;

import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.util.Map;



public class TerOp implements IExpression {

  private final TerOpType type;
  private final IExpression x;
  private final IExpression y;
  private final IExpression z;
  private SoftReference<BigInteger> vars;

  public TerOp(TerOpType type, IExpression x, IExpression y, IExpression z) {
    type.checkConstraints(x, y, z);
    this.type = type;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public String toString() {
      return type.sign + "(" + x + ", " + y + ", " + z + ")";
  }

  @Override
  public BigInteger getVars() {
    BigInteger v = vars == null ? null : vars.get();
    if (v == null) {
      v = x.getVars().or(y.getVars()).or(z.getVars());
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
		  IExpression sa = x.sub(v, c, ctx);
	      IExpression sb = y.sub(v, c, ctx);
	      IExpression sc = z.sub(v, c, ctx);
	      if (sa != x || sb != y || sc != z)
	        s = type.apply(sa, sb, sc);
	      else
	        s = this;
	      ctx.put(this, s);
	  }
	  return s;
	}
	
}
