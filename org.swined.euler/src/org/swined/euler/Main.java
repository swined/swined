package org.swined.euler;

import java.math.BigInteger;

public class Main {

  // axy + bx + cy = d (mod p)
  // returns a pair
  private static Pair solve(BigInteger a, BigInteger b, BigInteger c, BigInteger d, BigInteger p) {
    Pair r = null;
    for (BigInteger x = BigInteger.ZERO; x.compareTo(p) < 0; x = x.add(BigInteger.ONE))
      for (BigInteger y = BigInteger.ZERO; y.compareTo(p) < 0; y = y.add(BigInteger.ONE))
        if (a.multiply(x).multiply(y).add(b.multiply(x)).add(c.multiply(y)).mod(p).equals(d.mod(p)))
          if (r == null)
            r = new Pair(x, y);
          else 
            if (x.equals(r.b) && y.equals(r.a))
              continue;
            else
              return null;
    return r;
  }
  
  // axy + bx + cy = d
  private static Pair solve(BigInteger a, BigInteger b, BigInteger c, BigInteger d) {
    if (d.signum() == 0)
      return new Pair(BigInteger.ZERO, BigInteger.ZERO);
    if (d.signum() < 0)
      return null;
    BigInteger gcd = a.gcd(b).gcd(c);
    if (d.mod(gcd).equals(BigInteger.ZERO)) {
      a = a.divide(gcd);
      b = b.divide(gcd);
      c = c.divide(gcd);
      d = d.divide(gcd);
    } else {
      return null;
    }
    System.out.println(a);
    System.out.println(b);
    System.out.println(c);
    System.out.println(d);
    System.out.println();
    BigInteger p = BigInteger.valueOf(2);
    Pair r = null;
    while (p.compareTo(d) <= 0) {
      r = solve(a, b, c, d, p);
      if (r == null)
        p = p.nextProbablePrime();
      else
        break;
    }
    if (r == null)
      return null;
    System.out.println(p);
    System.out.println();
    // a(px + x0)(py+y0) + b(px+x0) + c(py+y0) = d
    // a(px + x0)(py+y0) + bpx + cpy = d - bx0 - cy0
    // appxy + (ay0 + b)px + (ax0 + c)py = d - bx0 - cy0 - ax0y0
    // apxy + (ay0 + b)x + (ax0 + c)y = (d - bx0 - cy0 - ax0y0) / p    
    BigInteger na = a.multiply(p);
    BigInteger nb = a.multiply(r.b).add(b);
    BigInteger nc = a.multiply(r.a).add(c);
    BigInteger nd = d.subtract(b.multiply(r.a)).subtract(c.multiply(r.b)).subtract(a.multiply(r.a).multiply(r.b)).divide(p);
    Pair s = solve(na, nb, nc, nd);
    if (s == null)
      return null;
    else
      return new Pair(p.multiply(s.a).add(r.a), p.multiply(s.b).add(r.b));
  }
  
  public static void main(String[] args) {
    BigInteger n = BigInteger.valueOf(10);
    n = n.nextProbablePrime();
    n = n.multiply(n.nextProbablePrime());
    System.out.println(solve(BigInteger.valueOf(1), BigInteger.ZERO, BigInteger.ZERO, n));
  }
  
}
