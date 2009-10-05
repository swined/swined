package mds;

import bool.int32.And;
import bool.int32.Const;
import bool.int32.Expression;
import bool.int32.Or;
import bool.int32.Variable;
import java.util.BitSet;

public class Main {

    protected static Expression xor(Expression a, Expression b) {
        return new Or(new And(a, b.invert()), new And(a.invert(), b));
    }

    protected static Expression shift(Expression x, int s) {
        return new And(x.rotate(s), Const.create(Const.xFFFFFFFF()).shift(s));
    }

    protected static Expression sum(Expression x, Expression y) {
        Expression r = Const.create(new BitSet());
        Expression b = Const.create(Const.xFFFFFFFF());
        for (int i = 1; i < 32; i++) {
                b = new And(b, shift(y, i));
                r = new Or(r, new And(shift(x, i), b));
        }
        return xor(xor(x, y), r);
    }

    public static void main(String[] args) {
        BitSet cx = new BitSet();
        for (int i = 1; i < 16; i++)
            cx.set(i, true);
        BitSet cy = new BitSet();
        for (int i = 16; i < 32; i++)
            cy.set(i, true);
        Expression x = Const.create(cx);
        Expression y = Const.create(cy);
        Expression e = sum(x, y);
        System.out.println(e.optimize());
    }

}