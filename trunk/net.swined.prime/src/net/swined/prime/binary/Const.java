package net.swined.prime.binary;

import java.util.Map;


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
    public Const not() {
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
    public Const not() {
      return ONE;
    }

    @Override
    public String toString() {
      return "0";
    }
    
  };

  public static Const get(boolean value) {
    return value ? ONE : ZERO;
  }
  
  @Override
  public IExpression sub(Var v, Const c, Map<IExpression, IExpression> map) {
    return this;
  }

  @Override
  public Var getVar() {
    return null;
  }
  
  @Override
  public abstract Const not();
  
}
