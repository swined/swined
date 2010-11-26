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
  
  private static IExpression[] xor(IExpression[] a, IExpression[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException();
    IExpression[] r = new IExpression[a.length];
    for (int i = 0; i < r.length; i++) {
        And x = new And(a[i], b[i].not());
        And y = new And(a[i].not(), b[i]);
        r[i] = new Or(x, y);
    }
    return r;
  }
  
  private static IExpression[] sum(IExpression[] a, IExpression[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException();
    IExpression[] q = new IExpression[a.length];
    q[0] = Const.ZERO;
    for (int i = 0; i < q.length - 1; i++)
        q[i + 1] = new Or(new And(a[i], b[i]), new And(q[i], new Or(a[i], b[i])));
    return xor(xor(a, b), q);
  }
  
  private static IExpression[] mul(IExpression[] a, IExpression[] b) {
    IExpression[] r = zero(a.length + b.length);
    for (int i = 0; i < a.length; i++) {
      IExpression[] t = zero(r.length);
      for (int j = 0; j < b.length; j++)
        t[i + j] = new And(a[i], b[j]);
      r = sum(r, t);
    }
    return r;
  }
  
  public static void main(String[] args) {
    
  }
  
}
