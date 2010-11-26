package net.swined.prime.binary;

public class Main {

  private static IExpression[] var(String n, int l) {
    IExpression[] e = new IExpression[l];
    for (int i = 0; i < l; i++)
      e[i] = new Var(n + i, false);
    return e;
  }

  private static IExpression[] zero(int l) {
    IExpression[] e = new IExpression[l];
    for (int i = 0; i < l; i++)
      e[i] = Const.ZERO;
    return e;
  }
  
  private static IExpression[] sum(IExpression[] a, IExpression[] b) {
    return null;
  }
  
  private static IExpression[] mul(IExpression[] a, IExpression[] b) {
    return null;
  }
  
  public static void main(String[] args) {
    
  }
  
}
