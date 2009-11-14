package fac;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Expression {

    private final List<Tuple<Integer, Multiplication>> muls;

    public Expression(List<Tuple<Integer, Multiplication>> muls) {
        List<Tuple<Integer, Multiplication>> m = new ArrayList();
        List<Tuple<Integer, Const>> c = new ArrayList();
        for (Tuple<Integer, Multiplication> t : muls)
            if (t.getY().getVars().isEmpty()) {
                c.add(new Tuple(t.getX(), t.getY().getConst()));
            } else {
                m.add(new Tuple(t.getX(), t.getY()));
            }
        if (!c.isEmpty())
            m.addAll(new ConstExpression(c).getItems());
        if (m.isEmpty())
            m.add(new Tuple(0, new Multiplication(new Const(0))));
        this.muls = m;
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

    public Expression substituteVariable(Variable v, Const c) {
        List<Tuple<Integer, Multiplication>> r = new ArrayList();
        for (Tuple<Integer, Multiplication> m : muls) {
            for (Tuple<Integer, Multiplication> t : m.getY().substituteVariable(v, c))
                r.add(new Tuple(m.getX() + t.getX(), t.getY()));
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

    public boolean isZero() {
        if (muls.size() == 1)
            return muls.get(0).getY().isZero();
        return false;
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
