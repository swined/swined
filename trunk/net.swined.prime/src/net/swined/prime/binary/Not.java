package net.swined.prime.binary;

public class Not extends Expression {

  private final IExpression x;
  
  public Not(IExpression x) {
    if (x instanceof Const || x instanceof Not)
      throw new IllegalArgumentException();
    this.x = x;
    x.getVars(vars);
  }
  
  @Override
  public IExpression not() {
    return x;
  }

  @Override
  public IExpression sub(Var v, Const c, SubContext ctx) {
    IExpression sub = ctx.not.get(this);
    if (sub == null) {
      IExpression sx = x.sub(v, c, ctx);
      sub = sx.not();
      ctx.not.put(this, sub);
    }
    return sub;    
  }

  @Override
  public String toString() {
    return "!" + x;
  }
  
  @Override
  public Var getVar() {
    return x.getVar();
  }
  
}
