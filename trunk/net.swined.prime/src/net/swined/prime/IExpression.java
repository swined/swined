package net.swined.prime;

import java.math.BigInteger;
import java.util.Map;

public interface IExpression {

    IExpression not();
  
    IExpression sub(int v, IExpression c, Map<IExpression, IExpression> ctx);

    BigInteger getVars();
    
}
