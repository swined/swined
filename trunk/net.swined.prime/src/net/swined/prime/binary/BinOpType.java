package net.swined.prime.binary;


public enum BinOpType {

  AND("&") {
    @Override
    public IExpression apply(IExpression a, IExpression b) {
      return Bin.and(a, b);
    }    
    
    @Override
    public void checkConstraints(IExpression a, IExpression b) {
      if (a instanceof Not && b instanceof Not)
        throw new IllegalArgumentException();
    }
    
  },

  OR("|") {
    @Override
    public IExpression apply(IExpression a, IExpression b) {
      return Bin.or(a, b);
    }
    
    @Override
    public void checkConstraints(IExpression a, IExpression b) {
      if (a instanceof Not && b instanceof Not)
        throw new IllegalArgumentException();
    }
  },
  
  XOR("^") {
    @Override
    public IExpression apply(IExpression a, IExpression b) {
      return Bin.xor(a, b);
    }
    
    @Override
    public void checkConstraints(IExpression a, IExpression b) {
      if (a instanceof Not && b instanceof Not)
        throw new IllegalArgumentException();
    }
    
  };
  
  public final String sign;
  
  private BinOpType(String sign) {
    this.sign = sign;
  }
  
  public abstract IExpression apply(IExpression a, IExpression b);
  
  public void checkConstraints(IExpression a, IExpression b) {
    
  }
  
}


