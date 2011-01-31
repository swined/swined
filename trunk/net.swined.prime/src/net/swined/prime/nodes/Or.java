package net.swined.prime.nodes;

import java.util.Map;

import net.swined.prime.binary.Bin;


public class Or extends Expression {

  public final IExpression a;
  public final IExpression b;

  public Or(IExpression a, IExpression b) {
      super(a, b);
      this.a = a;
      this.b = b;
  }

  @Override
  public String toString() {
      return "(" + a + " | " + b + ")";
  }

	@Override
	protected IExpression subImpl(int v, Const c,
			Map<IExpression, IExpression> ctx) {
		return Bin.or(a.sub(v, c, ctx), b.sub(v, c, ctx));
	}

}
