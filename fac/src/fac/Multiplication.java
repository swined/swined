package fac;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Multiplication {

    private int power;
    private Const c;
    private HashSet<Variable> vars;

    public Multiplication(int power, Variable v) {
        this.power = power;
        this.c = new Const(1);
        this.vars = new HashSet();
        this.vars.add(v);
    }

    public Multiplication(int power, Const c) {
        this.power = power;
        this.c = c;
        this.vars = new HashSet();
    }

    public Multiplication(int power, Const c, HashSet<Variable> vars) {
        this.power = power;
        this.c = c;
        this.vars = new HashSet(vars);
    }

    public int getPower() {
        return power;
    }

    public Expression multiply(Multiplication m) {
        List<Multiplication> r = new ArrayList();
        Tuple<Const, Const> nc = c.multiply(m.c);
        HashSet<Variable> v = new HashSet(vars);
        v.addAll(m.vars);
        if (!nc.getX().isZero())
            r.add(new Multiplication(power + m.power, nc.getX(), v));
        if (!nc.getY().isZero())
            r.add(new Multiplication(power + m.power + 1, nc.getY(), v));
        return new Expression(r);
    }

    @Override
    public String toString() {
        String r = "";
        if (power != 0)
            r += "10^" + power;
        if (!c.isOne())
            if (r.isEmpty())
                r += c;
            else
                r += " * " + c;
        for (Variable v : vars) {
            if (!r.isEmpty())
                r += " * ";
            r += v;
        }
        return r;
    }

}
