package net.swined.prime.binary;

import java.util.Map;

public class Not extends Expression {

  public final IExpression a;

  public Not(IExpression a) {
    super(a);
    this.a = a;
  }

  @Override
  public String toString() {
    return "!" + a;
  }

	@Override
	protected IExpression subImpl(int v, Const c,
			Map<IExpression, IExpression> ctx) {
		return Bin.not(a.sub(v, c, ctx));
	}
}
