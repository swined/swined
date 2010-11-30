package net.swined.prime.binary;

import java.util.Map;

public interface IExpression {

  IExpression and(IExpression e);
  IExpression or(IExpression e);
  IExpression not();
  IExpression sub(Var v, Const c, Map<IExpression, IExpression> map);
  
}
