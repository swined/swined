package net.swined.prime;

import java.util.Map;

public class Var implements IExpression {

	public final int name;

	public Var(int name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "x" + name;
	}

	@Override
	public IExpression sub(int v, Const c,
			Map<IExpression, IExpression> ctx) {
		return v == name ? c : this;
	}

	@Override
	public int getVar() {
		return name;
	}

	@Override
	public boolean hasVar(int v) {
		return v == name;
	}

  @Override
  public IExpression eo(int v, Map<IExpression, IExpression> co, Map<IExpression, IExpression> ca) {
    return v == name ? Const.ONE : this;
  }

  @Override
  public IExpression ea(int v, Map<IExpression, IExpression> co, Map<IExpression, IExpression> ca) {
    return v == name ? Const.ZERO : this;
  }

}
