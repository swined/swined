package fac;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Multiplication {

    private Const c;
    private HashSet<Variable> vars;

    public Multiplication(Variable v) {
        this.c = Const.create(1);
        this.vars = new HashSet();
        this.vars.add(v);
    }

    public Multiplication(Const c) {
        this.c = c;
        this.vars = new HashSet();
    }

    public Multiplication(Const c, HashSet<Variable> vars) {
        this.c = c;
        this.vars = new HashSet(vars);
    }

    public Const getConst() {
        return c;
    }

    public List<Variable> getVars() {
        return new ArrayList(vars);
    }

    public Multiplication excludeVariable(Variable v) {
        HashSet<Variable> nv = new HashSet(vars);
        nv.remove(v);
        return new Multiplication(c, nv);
    }

    public List<Tuple<Integer, Multiplication>> multiply(Const c) {
        Tuple<Const, Const> nc = this.c.multiply(c);
        List<Tuple<Integer, Multiplication>> r = new ArrayList();
        if (!nc.getX().isZero())
            r.add(new Tuple(0, new Multiplication(nc.getX(), vars)));
        if (!nc.getY().isZero())
            r.add(new Tuple(1, new Multiplication(nc.getY(), vars)));
        return r;
    }

    public List<Tuple<Integer, Multiplication>> substituteVariable(Variable v, Const c) {
        return excludeVariable(v).multiply(c);
    }

    public boolean isZero() {
        return c.isZero() && (vars.size() == 0);
    }

    @Override
    public String toString() {
        String r = "";
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
        if (r.isEmpty())
            return "0";
        else
            return r;
    }

}
