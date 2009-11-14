package fac;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Expression {

    private final List<Tuple<Integer, Multiplication>> muls;

    public Expression(List<Tuple<Integer, Multiplication>> muls) {
        this.muls = new ArrayList(muls);
    }
    
    public static Expression variableExpression(String name, int c) {
        List<Tuple<Integer, Multiplication>> muls = new ArrayList();
        for (int i = 0; i < c; i++)
            muls.add(new Tuple(i, new Multiplication(new Variable(name + i))));
        return new Expression(muls);
    }

    public Expression multiply(Expression e) {
        List<Tuple<Integer, Multiplication>> r = new ArrayList();
        for (Tuple<Integer, Multiplication> m1 : muls)
            for (Tuple<Integer, Multiplication> m2 : e.muls) {
                List<Tuple<Integer, Multiplication>> t = new ArrayList();
                Tuple<Const, Const> nc = m1.getY().getConst().multiply(m2.getY().getConst());
                HashSet<Variable> v = new HashSet(m1.getY().getVars());
                v.addAll(m2.getY().getVars());
                if (!nc.getX().isZero())
                    t.add(new Tuple(m1.getX() + m2.getX(), new Multiplication(nc.getX(), v)));
                if (!nc.getY().isZero())
                    t.add(new Tuple(m1.getX() + m2.getX() + 1, new Multiplication(nc.getY(), v)));
                r.addAll(t);
            }
        return new Expression(r);
    }

    public Mod10Expression mod10() {
        List<Multiplication> r = new ArrayList();
        for (Tuple<Integer, Multiplication> m : muls)
            if (m.getX() == 0)
                r.add(m.getY());
        return new Mod10Expression(r);
    }

    public Expression div10() {
        List<Tuple<Integer, Multiplication>> r = new ArrayList();
        for (Tuple<Integer, Multiplication> m : muls)
            if (m.getX() > 0)
                r.add(new Tuple(m.getX() - 1, m.getY()));
        return new Expression(r);
    }

    @Override
    public String toString() {
        String r = "";
        for (Tuple<Integer, Multiplication> m : muls) {
            if (!r.isEmpty())
                r += " + ";
            r += m;
        }
        return r;
    }

}
