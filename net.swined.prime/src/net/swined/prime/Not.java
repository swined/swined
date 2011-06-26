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
	public IExpression sub(int v, IExpression c,
			Map<IExpression, IExpression> ctx) {
		IExpression s = ctx.get(this);
		if (s == null) {
			IExpression x = e.sub(v, c, ctx);
			if (x != e)
				s = Bin.not(x);
			else
				s = this;
			ctx.put(this, s);
		}
		return s;
	}

	@Override
	public int getVar() {
		return e.getVar();
	}

}
