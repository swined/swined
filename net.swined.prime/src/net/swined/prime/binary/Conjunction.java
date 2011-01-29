package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;

public class Conjunction extends Expression {

    public final BigInteger sign;

    public Conjunction(BigInteger vars, BigInteger sign) {
        super(vars);
        if (vars.equals(BigInteger.ZERO))
            throw new IllegalArgumentException();
        this.sign = sign;
    }

    public static Conjunction var(int name, boolean negative) {
        BigInteger sign = negative ? BigInteger.ZERO.setBit(name) : BigInteger.ZERO;
        return new Conjunction(BigInteger.ZERO.setBit(name), sign);
    }

    @Override
    protected IExpression subImpl(int v, Const c, Map<IExpression, IExpression> ctx) {
        if (!vars.testBit(v))
            return this;
        BigInteger nv = vars.clearBit(v);
        if (sign.testBit(v) ^ (c == Const.ZERO)) {
            return Const.ZERO;
        } else {
            if (nv.equals(BigInteger.ZERO))
                return Const.ONE;
            else
                return new Conjunction(nv, sign);
        }
    }

    @Override
    protected IExpression notImpl() {
        return new Disjunction(vars, sign.setBit(vars.bitLength()).not());
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < vars.bitLength(); i++) {
        if (vars.testBit(i)) {
          if (sb.length() > 0)
            sb.append(" & ");
          if (sign.testBit(i))
            sb.append("!");
          sb.append("x");
          sb.append(i);
        }
      }
      return "(" + sb.toString() + ")";
    }

}
