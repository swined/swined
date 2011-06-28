package net.swined.prime;

import java.util.Map;

public class Or implements IExpression {

  private final int hash;
	private final IExpression a;
	private final IExpression b;

	public Or(IExpression a, IExpression b) {
		if (a instanceof Const || b instanceof Const)
			throw new IllegalArgumentException();
		if (a instanceof Var && b instanceof Var) {
			Var va = (Var) a;
			Var vb = (Var) b;
			if (va.name == vb.name)
				throw new IllegalArgumentException();
		}
		this.hash = a.hashCode() * b.hashCode();
		this.a = a;
		this.b = b;
	}

	@Override
	public String toString() {
		return "(" + a + " | " + b + ")";
	}

	@Override
	public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
		IExpression s = ctx.get(this);
		if (s == null) {
			s = Bin.or(a.sub(v, c, ctx), b.sub(v, c, ctx));
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
  public int hashCode() {
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (obj == this)
      return true;
    if (obj instanceof Or)
      return a.equals(((Or)obj).a) && b.equals(((Or)obj).b);
    else
      return false;
  }	
	
}
