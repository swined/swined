package mds;

import bool.int32.And;
import bool.int32.Const;
import bool.int32.Expression;
import bool.int32.Not;
import bool.int32.Or;

public class Main {

    protected static Expression xor(Expression x, Expression y) {
        return new Or(new And(x, new Not(y)), new And(new Not(x), y));
    }

    protected static Expression shift(Expression x, int s) {
        if (s >= 32)
            return new Const(0);
        return new And(x.rotate(s), new Const(0xFFFFFFFF << s));
    }

    protected static Expression sum(Expression x, Expression y) {
        x = x.toSCNF().optimize();
        y = y.toSCNF().optimize();
        System.out.println(x + " + " + y);
        if (x.toSCNF().isZero())
            return y;
        if (y.toSCNF().isZero())
            return x;
        return sum(xor(x, y), shift(new And(x, y), 1));
    }

    public static void main(String[] args) {
        Expression e = xor(new Const(0xFFFF), new Const(0xFFFF0000));
        System.out.println(e.toSCNF().optimize());
    }

}
