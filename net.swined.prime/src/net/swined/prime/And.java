package net.swined.prime;

import java.util.Map;

public class And implements IExpression {

	private final IExpression a;
	private final IExpression b;

	public And(IExpression a, IExpression b) {
		if (a instanceof Const || b instanceof Const)
			throw new IllegalArgumentException();
		if (a instanceof WTF && b instanceof WTF)
			throw new IllegalArgumentException();
		if (a instanceof Var && b instanceof Var) {
			Var va = (Var) a;
			Var vb = (Var) b;
			if (va.name == vb.name)
				throw new IllegalArgumentException();
		}
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
	public IExpression wxsub(int v, Map<IExpression, IExpression> ctx) {
		IExpression s = ctx.get(this);
		if (s == null) {
			s = Bin.and(a.wxsub(v, ctx), b.wxsub(v, ctx));
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
