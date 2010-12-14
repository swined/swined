package net.swined.prime.binary;

public interface IExpression {

  IExpression and(IExpression e);
  IExpression or(IExpression e);
  IExpression xor(IExpression e);
  IExpression not();
  IExpression m2(IExpression x, IExpression y);
  IExpression sub(Var v, Const c, SubContext ctx);
  Var getVar();
  
}
