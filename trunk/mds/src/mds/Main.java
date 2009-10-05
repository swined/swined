package mds;

import bool.int32.And;
import bool.int32.Const;
import bool.int32.Expression;
import bool.int32.Or;
import bool.int32.Variable;

public class Main {

    protected static Expression xor(Expression a, Expression b) {
        return new Or(And.create(a, b.invert()), And.create(a.invert(), b));
    }

    protected static Expression shift(Expression x, int s) {
        if (s >= 32)
            return Const.create(0);
        return And.create(x.rotate(s), Const.create(0xFFFFFFFF << s));
    }

    protected static Expression sum(Expression x, Expression y) {
        x = x.optimize();
        y = y.optimize();
        System.out.println(x + " + " + y);
        if (x.isFalse())
            return y;
        if (y.isFalse())
            return x;
        return sum(xor(x, y), shift(And.create(x, y), 1));
    }

    public static void main(String[] args) {
        Expression x = Const.create(0x80000000);
        Expression y = Variable.create("x");
        Expression e = sum(x, y);
        System.out.println(e);
    }

}
