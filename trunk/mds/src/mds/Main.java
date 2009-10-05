package mds;

import bool.int32.And;
import bool.int32.Const;
import bool.int32.Expression;
import bool.int32.Or;
import bool.int32.Variable;

public class Main {

    protected static Expression xor(Expression a, Expression b) {
        return new Or(new And(a, b.invert()), new And(a.invert(), b));
    }

    protected static Expression shift(Expression x, int s) {
        if (s >= 32)
            return new Const(0);
        return new And(x.rotate(s), new Const(0xFFFFFFFF << s));
    }

    protected static Expression sum(Expression x, Expression y) {
        x = x.optimize();
        y = y.optimize();
        System.out.println(x + " + " + y);
        if (x.isFalse())
            return y;
        if (y.isFalse())
            return x;
        return sum(xor(x, y), shift(new And(x, y), 1));
    }

    public static void main(String[] args) {
        Expression x = new Const(0x80000000);
        Expression y = new Variable("x");
        Expression e = sum(x, y);
        System.out.println(e);
    }

}
