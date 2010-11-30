package net.swined.prime.binary;


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
  
  public IExpression sub(Var v, Const c) {
	return this;
  }
  
}
