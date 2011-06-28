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
	public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
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
	public int hashCode() {
	  return name;
	}
	
	@Override
	public boolean equals(Object obj) {
	  if (obj == null)
	    return false;
    if (obj == this)
      return true;
	  if (obj instanceof Var)
	    return name == ((Var)obj).name;
	  else
	    return false;
	}
	
}
