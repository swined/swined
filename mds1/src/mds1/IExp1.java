package mds1;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.HashMap;

public interface IExp1 {

    IExp1 and(IExp1 exp);
    IExp1 or(IExp1 exp);
    IExp1 xor(IExp1 exp);
    IExp1 not();
    void setNot(IExp1 not);
    IExp1 sub(HashMap<IExp1, IExp1> context, Var1 v, Const1 c);
    BigInteger depends(HashMap<IExp1, BigInteger> context, Var1 v);
    boolean hasDisjunctions();
    Var1 getVar();
    void print(PrintStream out);
    int depth();

}
