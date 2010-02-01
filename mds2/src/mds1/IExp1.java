package mds1;

import java.util.HashMap;

public interface IExp1 {

    IExp1 optimize(HashMap<IExp1, IExp1> context);
    IExp1 sub(HashMap<IExp1, IExp1> context, Var1 v, Const1 c);

}
