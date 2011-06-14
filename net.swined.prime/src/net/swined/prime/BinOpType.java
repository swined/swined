package net.swined.prime;


public enum BinOpType {

  AND("&") {
    @Override
    public IExpression apply(IExpression a, IExpression b) {
      return Bin.and(a, b);
    }

    @Override
    public IExpression invert(IExpression a, IExpression b) {
      return Bin.or(a.not(), b.not());
    }    
    
  },

  OR("|") {
    @Override
    public IExpression apply(IExpression a, IExpression b) {
      return Bin.or(a, b);
    }

    @Override
    public IExpression invert(IExpression a, IExpression b) {
      return Bin.and(a.not(), b.not());
    }
    
  },
  
  XOR("^") {
    @Override
    public IExpression apply(IExpression a, IExpression b) {
      return Bin.xor(a, b);
    }

    @Override
    public IExpression invert(IExpression a, IExpression b) {
      return Bin.xor(a, b);
    }
    
  };
  
  public final String sign;
  
  private BinOpType(String sign) {
    this.sign = sign;
  }
  
  public abstract IExpression apply(IExpression a, IExpression b);
  public abstract IExpression invert(IExpression a, IExpression b);
  
  public void checkConstraints(IExpression a, IExpression b) {
    if (a instanceof Const || b instanceof Const)
      throw new IllegalArgumentException();
    if (a instanceof Var && b instanceof Var) {
      Var va = (Var)a;
      Var vb = (Var)b;
      if (va.name == vb.name)
        throw new IllegalArgumentException();
      if (va.sign && vb.sign)
        throw new IllegalArgumentException();
    }
  }
  
}


