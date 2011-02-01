package org.swined.dsa;

import java.math.BigInteger;
import java.util.Map;

public interface IExpression {

    IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx);

    BigInteger getVars();
    
}
