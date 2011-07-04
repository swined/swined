package net.swined.prime;

import java.math.BigInteger;
import java.util.Map;

public class Not implements IExpression {

	public final IExpression e;

	public Not(IExpression e) {
		if (e instanceof Const || e instanceof Not)
			throw new IllegalArgumentException();
		this.e = e;
	}

	@Override
	public BigInteger complexity(Map<IExpression, BigInteger> ctx) {
		return e.complexity(ctx);
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
	public String toString() {
		return "!" + e;
	}

}
