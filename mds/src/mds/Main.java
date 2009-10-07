package mds;

import bool.int32.And;
import bool.int32.Const;
import bool.int32.Expression;
import bool.int32.Or;
import bool.int32.Variable;

public class Main {

    protected static Expression xor(Expression a, Expression b) {
        return new Or(new And(a, b.invert()), new And(a.invert(), b)).optimize();
    }

    protected static Expression shift(Expression x, int s) {
        return new And(x.rotate(s), Const.TRUE().shift(s)).optimize();
    }

    protected static Expression sum(Expression x, Expression y) {
        Expression r = Const.FALSE();
        Expression b = Const.TRUE();
        for (int i = 1; i < 32; i++) {
                b = new And(b, shift(y, i));
                r = new Or(r, new And(shift(x, i), b));
        }
        return xor(xor(x, y), r);
    }

    public static void main(String[] args) {
        Expression x = Variable.create("x");
        Expression y = Variable.create("y");
        Expression e = sum(sum(x, y.invert()), sum(x.invert(), y));
        System.out.println(e.optimize());
    }

}