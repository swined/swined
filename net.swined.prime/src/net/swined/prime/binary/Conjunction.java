package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;

public class Conjunction extends Expression {

    private final BigInteger sign;

    public Conjunction(BigInteger vars, BigInteger sign) {
        super(vars);
        this.sign = sign;
    }

    public static Conjunction var(int name, boolean negative) {
        BigInteger sign = negative ? BigInteger.ZERO.setBit(name) : BigInteger.ZERO;
        return new Conjunction(BigInteger.ZERO.setBit(name), sign);
    }

    @Override
    protected IExpression subImpl(int v, Const c, Map<IExpression, IExpression> ctx) {
        if (vars.bitCount() == 1) {
            return sign.testBit(v) ? c.not() : c;
        }
        return null;
    }

    @Override
    public IExpression not() {
        return new Not(this);
    }



}
