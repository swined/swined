package fac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Equation {

    private final HashMap<Variable, Const> vars;
    private final Expression left;
    private final ConstExpression right;

    public Equation(Expression left, ConstExpression right) {
        this.vars = new HashMap();
        this.left = left;
        this.right = right;
    }

    public Equation(HashMap<Variable, Const> vars, Expression left, ConstExpression right) {
        this.vars = new HashMap(vars);
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
        return new Equation(nv, left.substituteVariable(v, c), right);
    }

    public List<Equation> solve() throws Exception {
        List<Equation> r = new ArrayList();
        Set<HashMap<Variable, Const>> solutions = mod10().solve();
        if (solutions.size() == 0)
            return null;
        for (HashMap<Variable, Const> solution : solutions) {
            Equation eq = this;
            for (Variable v : solution.keySet())
                eq = eq.substituteVariable(v, solution.get(v));
            r.add(eq.div10());
        }
        return r;
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
