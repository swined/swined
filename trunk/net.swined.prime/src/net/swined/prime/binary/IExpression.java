package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;

public interface IExpression {

    IExpression and(IExpression e);

    IExpression or(IExpression e);

    IExpression xor(IExpression e);

    IExpression not();

    IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx);

    IExpression sub(int v, Const c);

    BigInteger getVars();
}
