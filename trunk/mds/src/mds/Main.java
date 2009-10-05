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
        return new And(x.rotate(s), Const.create(0xFFFFFFFF << s));
    }

    protected static Expression sum(Expression x, Expression y) {
        Expression r = Const.create(0);
        Expression b = Const.create(0xFFFFFFFF);
        for (int i = 1; i < 32; i++) {
                b = new And(b, shift(y, i));
                r = new Or(r, new And(shift(x, i), b));
        }
        return xor(xor(x, y), r);
    }

    public static void main(String[] args) {
        Expression x = Const.create(0x00000001);
        Expression y = Const.create(0xF0FF0001);
        Expression e = sum(x, y);
        System.out.println(e.optimize());
    }

}