package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;

public interface IExpression {

    IExpression and(IExpression e);

    IExpression or(IExpression e);

    IExpression xor(IExpression e);

    IExpression not();

    IExpression m2(IExpression x, IExpression y);

    IExpression sub(Var v, Const c, Map<IExpression, IExpression> ctx);

    IExpression sub(Var v, Const c);

    BigInteger getVars();
}
