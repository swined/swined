package fac;

import java.util.ArrayList;
import java.util.List;

public class Expression {

    private final List<Multiplication> muls;

    public Expression(List<Multiplication> muls) {
        this.muls = new ArrayList(muls);
    }

    public static ConstExpression constExpression(int c[]) {
        List<Tuple<Integer, Const>> muls = new ArrayList();
        for (int i = 0; i < c.length; i++)
            muls.add(new Tuple<Integer, Const>(i, new Const(c[i])));
        return new ConstExpression(muls);
    }

    public static Expression variableExpression(String name, int c) {
        List<Multiplication> muls = new ArrayList();
        for (int i = 0; i < c; i++)
            muls.add(new Multiplication(i, new Variable(name + i)));
        return new Expression(muls);
    }

    public Expression multiply(Expression e) {
        List<Multiplication> r = new ArrayList();
        for (Multiplication m1 : muls)
            for (Multiplication m2 : e.muls)
                r.addAll(m1.multiply(m2).muls);
        return new Expression(r);
    }

    public Expression mod10() {
        List<Multiplication> r = new ArrayList();
        for (Multiplication m : muls)
            if (m.getPower() == 0)
                r.add(m);
        return new Expression(r);
    }

    @Override
    public String toString() {
        String r = "";
        for (Multiplication m : muls) {
            if (!r.isEmpty())
                r += " + ";
            r += m;
        }
        return r;
    }

}
