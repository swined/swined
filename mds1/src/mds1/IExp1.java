package mds1;

import java.util.HashSet;

public interface IExp1 {

    IExp1 and(IExp1 exp);
    IExp1 or(IExp1 exp);
    IExp1 xor(IExp1 exp);
    IExp1 not();
    void getVars(HashSet<Var1> vars);

}
