package mds1;

import java.io.PrintStream;
import java.util.HashMap;

public interface IExp1 {

    IExp1 and(IExp1 exp);
    IExp1 or(IExp1 exp);
    IExp1 xor(IExp1 exp);
    IExp1 not();
    void setNot(IExp1 not);
    IExp1 sub(HashMap<IExp1, IExp1> context, Var1 v, Const1 c);
    boolean hasDisjunctions();
    Var1 getVarA();
    Var1 getVarB();
    void print(PrintStream out);

}
