package net.swined.prime.binary;

import java.util.Map;
import java.util.Set;


public enum Const implements IExpression {

  ONE {

    @Override
    public IExpression and(IExpression e) {
      return e;
    }

    @Override
    public IExpression or(IExpression e) {
      return ONE;
    }

    @Override
    public IExpression not() {
      return ZERO;
    }

    @Override
    public String toString() {
      return "1";
    }
    
  },
  
  ZERO {

    @Override
    public IExpression and(IExpression e) {
      return ZERO;
    }

    @Override
    public IExpression or(IExpression e) {
      return e;
    }

    @Override
    public IExpression not() {
      return ONE;
    }

    @Override
    public String toString() {
      return "0";
    }
    
  };

  @Override
  public IExpression sub(Var v, Const c, Map<IExpression, IExpression> map) {
    return this;
  }

  @Override
  public void getVars(Set<Var> vars) {
  }
  
}
