package net.swined.prime;

import java.util.Map;

public class Not implements IExpression {

	public final IExpression e;

	public Not(IExpression e) {
		if (e instanceof Const || e instanceof Not)
			throw new IllegalArgumentException();
		this.e = e;
	}

	@Override
	public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
		return Bin.not(e.sub(v, c, ctx));
	}

	@Override
	public int getVar() {
		return e.getVar();
	}

	@Override
	public boolean hasVar(int v) {
		return e.hasVar(v);
	}

	@Override
	public String toString() {
		return "!" + e;
	}

}
