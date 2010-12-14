package net.swined.prime.binary;

import java.util.HashMap;
import java.util.Map;

public class SubContext {

    public final Map<And, IExpression> and = new HashMap<And, IExpression>();
    public final Map<Or, IExpression> or = new HashMap<Or, IExpression>();
    public final Map<Xor, IExpression> xor = new HashMap<Xor, IExpression>();
    public final Map<Not, IExpression> not = new HashMap<Not, IExpression>();
    public final Map<M2, IExpression> m2 = new HashMap<M2, IExpression>();

}
