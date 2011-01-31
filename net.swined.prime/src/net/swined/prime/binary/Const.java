package net.swined.prime.binary;

import java.math.BigInteger;
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
      public IExpression xor(IExpression a) {
        return Bin.not(a);
      }
      
      @Override
      public Const not() {
        return ZERO;
      }
      
      @Override
      public IExpression m2(IExpression a, IExpression b) {
        return Bin.or(a, b);
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
      public IExpression xor(IExpression a) {
        return a;
      }
      
      @Override
      public Const not() {
        return ONE;
      }
      
      @Override
      public IExpression m2(IExpression a, IExpression b) {
        return Bin.and(a, b);
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
    public BigInteger getVars() {
        return BigInteger.ZERO;
    }

    public abstract Const not();
    public abstract IExpression and(IExpression a);
    public abstract IExpression or(IExpression a);
    public abstract IExpression xor(IExpression a);
    public abstract IExpression m2(IExpression a, IExpression b);
    
}
