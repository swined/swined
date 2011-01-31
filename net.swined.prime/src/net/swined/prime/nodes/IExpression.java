package net.swined.prime.nodes;

import java.math.BigInteger;
import java.util.Map;

public interface IExpression {

    IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx);

    IExpression sub(int v, Const c);

    BigInteger getVars();
    
}
