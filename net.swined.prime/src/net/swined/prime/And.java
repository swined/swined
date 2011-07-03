package net.swined.prime;

import java.util.Map;

public class And implements IExpression {

	private final Var a;
	private final IExpression b;

	public And(Var a, IExpression b) {
		if (b instanceof Const)
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
	public boolean hasVar(int v) {
		return a.hasVar(v) || b.hasVar(v);
	}

}
