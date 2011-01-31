package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;

public interface IExpression {

    IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx);

    IExpression sub(int v, Const c);

    BigInteger getVars();
    
}
