package net.swined.prime.binary;

public enum Const implements IExpression {

  TRUE {

    @Override
    public IExpression and(IExpression e) {
      return e;
    }

    @Override
    public IExpression or(IExpression e) {
      return TRUE;
    }

    @Override
    public IExpression not() {
      return FALSE;
    }
    
  },
  
  FALSE {

    @Override
    public IExpression and(IExpression e) {
      return FALSE;
    }

    @Override
    public IExpression or(IExpression e) {
      return e;
    }

    @Override
    public IExpression not() {
      return TRUE;
    }
    
  };
  
}
