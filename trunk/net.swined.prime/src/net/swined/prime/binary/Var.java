package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;

public class Var extends Expression {

    public final int name;
    private final boolean negative;

    public Var(int name, boolean negative) {
        super(BigInteger.ZERO.setBit(name));
        this.name = name;
        this.negative = negative;
    }

    @Override
    public IExpression not() {
        return new Var(name, !negative);
    }

    @Override
    protected IExpression subImpl(Var v, Const c, Map<IExpression, IExpression> ctx) {
        if (name == v.name) {
            return negative == v.negative ? c : c.not();
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return "x" + name;
    }
}
