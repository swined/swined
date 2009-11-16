package fac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Equation {

    private final ImmutableMap<Variable, Const> vars;
    private final Expression left;
    private final ConstExpression right;

    public Equation(Expression left, ConstExpression right) {
        this.vars = new ImmutableMap(new HashMap());
        this.left = left;
        this.right = right;
    }

    public Equation(ImmutableMap<Variable, Const> vars, Expression left, ConstExpression right) {
        this.vars = vars;
        this.left = left;
        this.right = right;
    }

    public Mod10Equation mod10() {
        return new Mod10Equation(left.mod10(), right.mod10());
    }

    public Equation div10() {
        return new Equation(vars, left.div10(), right.div10());
    }

    public Equation substituteVariable(Variable v, Const c) {
        HashMap<Variable, Const> nv = new HashMap(vars);
        nv.put(v, c);
        return new Equation(new ImmutableMap(nv), left.substituteVariable(v, c), right);
    }

    public List<Equation> solve() throws Exception {
        List<Equation> r = new ArrayList();
        Mod10Equation mod10 = mod10();
        for (HashMap<Variable, Const> solution : mod10.solve()) {
            Equation eq = this;
            for (Variable v : solution.keySet())
                eq = eq.substituteVariable(v, solution.get(v));
            Equation div10 = eq.div10();
            r.add(div10);
        }
        return r;
    }

    public boolean isSolution() {
        return left.isZero() && right.isZero();
    }

    @Override
    public String toString() {
        String r = left + " = " + right;
        if (!vars.isEmpty()) {
            r += " // ";
            for (Variable v : vars.keySet())
                r += v + " = " + vars.get(v) + "; ";
        }
        return r;
    }

}
