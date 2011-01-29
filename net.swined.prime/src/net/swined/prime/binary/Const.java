package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;

public enum Const implements IExpression {

    ONE {

        @Override
        public IExpression and(IExpression e) {
            return e;
        }

        @Override
        public IExpression or(IExpression e) {
            return ONE;
        }

        @Override
        public Const not() {
            return ZERO;
        }

        @Override
        public String toString() {
            return "1";
        }
    },
    ZERO {

        @Override
        public IExpression and(IExpression e) {
            return ZERO;
        }

        @Override
        public IExpression or(IExpression e) {
            return e;
        }

        @Override
        public Const not() {
            return ONE;
        }

        @Override
        public String toString() {
            return "0";
        }
    };

    @Override
    public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
        return this;
    }

    @Override
    public IExpression sub(int v, Const c) {
        return this;
    }

    @Override
    public BigInteger getVars() {
        return BigInteger.ZERO;
    }

    @Override
    public BigInteger complexity() {
        return BigInteger.ZERO;
    }

}
