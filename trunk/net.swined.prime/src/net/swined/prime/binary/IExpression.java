package net.swined.prime.binary;

import java.math.BigInteger;

public interface IExpression {

  IExpression and(IExpression e);
  IExpression or(IExpression e);
  IExpression not();
  IExpression sub(Var v, Const c);
  BigInteger complexity();
  
}
