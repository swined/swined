package net.swined.prime;

import java.math.BigInteger;
import java.util.Map;

public class And implements IExpression {

	private final IExpression a;
	private final IExpression b;

	public And(IExpression a, IExpression b) {
		if (a instanceof Const || b instanceof Const)
			throw new IllegalArgumentException();
		this.a = a;
		this.b = b;
	}

	@Override
	public String toString() {
		return "(" + a + " & " + b + ")";
	}

	@Override
	public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
		IExpression s = ctx.get(this);
		if (s == null) {
			s = Bin.and(a.sub(v, c, ctx), b.sub(v, c, ctx));
			ctx.put(this, s);
		}
		return s;
	}

	@Override
	public int getVar() {
		return a.getVar();
	}

	@Override
	public BigInteger complexity(Map<IExpression, BigInteger> ctx) {
		BigInteger s = ctx.get(this);
		if (s == null) {
			s = a.complexity(ctx).add(b.complexity(ctx));
			ctx.put(this, s);
		}
		return s;
	}

}
