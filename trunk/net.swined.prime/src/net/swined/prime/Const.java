package net.swined.prime;

import java.util.Map;


public enum Const implements IExpression {

    ONE {

      @Override
      public IExpression and(IExpression a) {
        return a;
      }

      @Override
      public IExpression or(IExpression a) {
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
      public IExpression and(IExpression a) {
        return ZERO;
      }

      @Override
      public IExpression or(IExpression a) {
        return a;
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

    @Override
    public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
        return this;
    }

    @Override
    public int getVar() {
    	return -1;
    }

    @Override
    public boolean hasVar(int v) {
    	return false;
    }
    
    @Override
    public IExpression eo(int v, Map<IExpression, IExpression> co, Map<IExpression, IExpression> ca) {
      return this;
    }

    @Override
    public IExpression ea(int v, Map<IExpression, IExpression> co, Map<IExpression, IExpression> ca) {
      return this;
    }
    
    public abstract Const not();
    public abstract IExpression and(IExpression a);
    public abstract IExpression or(IExpression a);
    
}
