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
        return new And(x.rotate(s), new Const(0xFFFFFFFF >> (32 - s)));
    }

    protected static Expression sum(Expression x, Expression y) {
        
    }

    public static void main(String[] args) {
        int[] r = {
            7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,
            5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,
            4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,
            6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21
        };
        Expression[] x = {
            new Variable("x1"),
            new Variable("x2"),
            new Variable("x3"),
            new Variable("x4"),
        };
        Expression a = new Const(0x67452301);
        Expression b = new Const(0xEFCDAB89);
        Expression c = new Const(0x98BADCFE);
        Expression d = new Const(0x10325476);
        Expression t = new Const(1);
        Expression e = F(a, b, x[1]);
        System.out.println(shift(t, 1).optimize());
        System.out.println(e.optimize());
        System.out.println(e.invert().optimize());
        System.out.println(shift(xor(e, e.invert()).optimize(), 4).optimize().toSCNF().optimize());
    }

}
