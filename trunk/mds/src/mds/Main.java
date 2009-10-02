package mds;

import bool.int32.And;
import bool.int32.Const;
import bool.int32.Expression;
import bool.int32.Variable;

public class Main {

    public static void main(String[] args) {
        Expression e = new And(new Variable("x", true, 17), new Const(29));
        System.out.println(e);
        System.out.println(e.invert());
    }

}
