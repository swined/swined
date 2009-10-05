package mds;

import bool.int32.And;
import bool.int32.Const;
import bool.int32.Expression;
import bool.int32.Or;
import bool.int32.Variable;

public class Main {

    protected static Expression F(Expression x, Expression y, Expression z) {
        return new Or(new And(x, y), new And(x.invert(), z));
    }

    protected static Expression xor(Expression x, Expression y) {
        return new Or(new And(x, y.invert()), new And(x.invert(), y));
    }

    protected static Expression shift(Expression x, int s) {
        if (s >= 32)
            return new Const(0);
        return new And(x.rotate(s), new Const(0xFFFFFFFF << s));
    }

    protected static Expression rshift(Expression x, int s) {
        if (s >= 32)
            return new Const(0);
        return new And(x.rotate(-s), new Const(0xFFFFFFFF >> s));
    }

    protected static Expression sum(Expression x, Expression y, int a, int b, int c, int d) {
        x = x.toSCNF().optimize();
        y = y.toSCNF().optimize();
        System.out.println(a + " " + b + " " + c + " " + d + " " + x + " + " + y);
        if (a > b)
            return y;
        if (c > d)
            return x;
        return sum(xor(x, y), shift(new And(x, y), 1), a, b, c + 1, d);
    }

    // a + b = a ^ b + (a & b) << 1

    public static void main(String[] args) {
        Expression e = sum(new Variable("x"), new Variable("y"), 0, 31, 0, 31);
    }

}
