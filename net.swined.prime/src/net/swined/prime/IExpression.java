package net.swined.prime;

import java.util.Map;

public interface IExpression {

    IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx);
    int getVar();
    
}
