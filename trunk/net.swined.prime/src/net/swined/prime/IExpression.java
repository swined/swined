package net.swined.prime;

import java.util.Map;

public interface IExpression {

    IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx);
    IExpression wxsub(int v, Map<IExpression, IExpression> ctx);
    int getVar();
    boolean hasVar(int v);
    
}
