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
    for (int i = 0; i < r.length; i++)
      r[i] = a[i].and(b[i].not()).or(a[i].not().and(b[i]));
    return r;
  }
  
  private static IExpression[] sum(IExpression[] a, IExpression[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException();
    IExpression[] q = new IExpression[a.length];
    q[0] = Const.ZERO;
    for (int i = 0; i < q.length - 1; i++)
        q[i + 1] = a[i].and(b[i]).or(q[i].and(a[i].or(b[i])));
    return xor(xor(a, b), q);
  }
  
  private static IExpression[] mul(IExpression[] a, IExpression[] b) {
    IExpression[] r = zero(a.length + b.length);
    for (int i = 0; i < a.length; i++) {
      IExpression[] t = zero(r.length);
      for (int j = 0; j < b.length; j++)
        t[i + j] = a[i].and(b[j]);
      r = sum(r, t);
    }
    return r;
  }
  
  public static void main(String[] args) {
    IExpression[] a = var("a", 10);
    IExpression[] b = var("b", 10);
    IExpression[] c = mul(a, b);
    System.out.println(c);
//    for (IExpression e : c)
//      System.out.println(e);
  }
  
}
