package net.swined.prime.nodes;

import java.math.BigInteger;
import java.util.Map;

import net.swined.prime.binary.Bin;

public class Not implements IExpression {

  public final IExpression a;

  public Not(IExpression a) {
    this.a = a;
  }

  @Override
  public String toString() {
    return "!" + a;
  }

  @Override
  public BigInteger getVars() {
    return a.getVars();
  }
  
	@Override
	public IExpression sub(int v, Const c,
			Map<IExpression, IExpression> ctx) {
    if (!getVars().testBit(v))
      return this;
    IExpression s = ctx.get(this);
    if (s == null) {
      IExpression sa = a.sub(v, c, ctx);
      if (sa != a)
        s = Bin.not(sa);
      else
        s = this;
      ctx.put(this, s);
    }
    return s;
	}
}
