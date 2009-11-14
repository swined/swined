package fac;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Multiplication {

    private Const c;
    private HashSet<Variable> vars;

    public Multiplication(Variable v) {
        this.c = new Const(1);
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
        return r;
    }

}
