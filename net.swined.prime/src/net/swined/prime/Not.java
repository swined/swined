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
	public IExpression sub(int v, Const c,
			Map<IExpression, IExpression> ctx) {
		IExpression s = ctx.get(this);
		if (s == null) {
			s = Bin.not(e.sub(v, c, ctx));
			ctx.put(this, s);
		}
		return s;
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
  public IExpression eo(int v, Map<IExpression, IExpression> co, Map<IExpression, IExpression> ca) {
    return Bin.not(e.ea(v, co, ca));
  }

  @Override
  public IExpression ea(int v, Map<IExpression, IExpression> co, Map<IExpression, IExpression> ca) {
    return Bin.not(e.eo(v, co, ca));
  }

}
