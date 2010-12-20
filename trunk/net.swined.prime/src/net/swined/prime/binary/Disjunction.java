package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;

public class Disjunction extends Expression {

    public final BigInteger sign;

    public Disjunction(BigInteger vars, BigInteger sign) {
        super(vars);
        if (vars.equals(BigInteger.ZERO))
            throw new IllegalArgumentException();
        this.sign = sign;
    }

    public static Disjunction var(int name, boolean negative) {
        BigInteger sign = negative ? BigInteger.ZERO.setBit(name) : BigInteger.ZERO;
        return new Disjunction(BigInteger.ZERO.setBit(name), sign);
    }

    @Override
    protected IExpression subImpl(int v, Const c, Map<IExpression, IExpression> ctx) {
        if (!vars.testBit(v))
            return this;
        BigInteger nv = vars.clearBit(v);
        if (sign.testBit(v) ^ (c == Const.ONE)) {
            return Const.ONE;
        } else {
            if (nv.equals(BigInteger.ZERO))
                return Const.ZERO;
            else
                return new Disjunction(nv, sign);
        }
    }

    @Override
    public IExpression not() {
        return new Conjunction(vars, sign.setBit(vars.bitLength()).not());
    }



}
