package net.swined.prime;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Map;
import java.util.Set;

public interface IExpression {

	BigInteger complexity(Map<IExpression, BigInteger> ctx);
    IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx);
    int getVar();
    void getVars(BitSet vars, Set<IExpression> ctx);
    boolean hasVar(int v);
    
}
