package net.swined.prime;

import java.math.BigInteger;
import java.util.Map;

public interface IExpression {

	BigInteger complexity(Map<IExpression, BigInteger> ctx);
    IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx);
    int getVar();
    
}
