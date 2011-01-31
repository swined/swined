package net.swined.prime.nodes;

import java.math.BigInteger;
import java.util.Map;

import net.swined.prime.binary.Bin;

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
      public IExpression not() {
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
    public IExpression sub(int v, Const c) {
        return this;
    }

    @Override
    public BigInteger getVars() {
        return BigInteger.ZERO;
    }

    public abstract IExpression not();
    public abstract IExpression and(IExpression a);
    public abstract IExpression or(IExpression a);
    public abstract IExpression xor(IExpression a);
    
}
