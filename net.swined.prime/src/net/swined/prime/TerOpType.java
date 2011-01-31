package net.swined.prime;


public enum TerOpType {

  M2("m2") {
	  
    @Override
    public IExpression apply(IExpression a, IExpression b, IExpression c) {
      return Bin.m2(a, b, c);
    }    
    
  },

  XOR("xor") {
	  
	    @Override
	    public IExpression apply(IExpression a, IExpression b, IExpression c) {
	      return Bin.xor(a, b, c);
	    }    
	    
	  },
  
  GE("ge") { // a & !b | (a ^ !b) & g
    
    @Override
    public IExpression apply(IExpression a, IExpression b, IExpression c) {
      return Bin.ge(a, b, c);
    }    
    
  };

  public final String sign;
  
  private TerOpType(String sign) {
    this.sign = sign;
  }
  
  public abstract IExpression apply(IExpression a, IExpression b, IExpression c);
  
  public void checkConstraints(IExpression a, IExpression b, IExpression c) {
    if (a instanceof Const || b instanceof Const || c instanceof Const)
      throw new IllegalArgumentException();
  }
  
}


