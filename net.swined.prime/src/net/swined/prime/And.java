package net.swined.prime;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Map;
import java.util.Set;

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
	public boolean hasVar(int v) {
		return a.hasVar(v) || b.hasVar(v);
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

	@Override
	public void getVars(BitSet vars, Set<IExpression> ctx) {
		if (!ctx.contains(this)) {
			a.getVars(vars, ctx);
			b.getVars(vars, ctx);
			ctx.add(this);
		}
	}

}
