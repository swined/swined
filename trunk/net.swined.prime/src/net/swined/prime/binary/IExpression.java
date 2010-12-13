package net.swined.prime.binary;

import java.util.Map;
import java.util.Set;

public interface IExpression {

  IExpression and(IExpression e);
  IExpression or(IExpression e);
  IExpression not();
  IExpression sub(Var v, Const c, Map<IExpression, IExpression> map);
  void getVars(Set<Var> vars);
  Var getVar();
  
}
