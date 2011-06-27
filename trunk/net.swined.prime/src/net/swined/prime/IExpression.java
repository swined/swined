package net.swined.prime;

import java.util.Map;

public interface IExpression {

    IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx);
    IExpression eo(int v, Map<IExpression, IExpression> co, Map<IExpression, IExpression> ca);
    IExpression ea(int v, Map<IExpression, IExpression> co, Map<IExpression, IExpression> ca);
    int getVar();
    boolean hasVar(int v);
    
}
