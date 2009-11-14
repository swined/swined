package fac;

import java.util.HashMap;

public class Equation {

    private final HashMap<Variable, Const> vars;
    private final Expression left;
    private final Expression right;

    public Equation(Expression left, Expression right) {
        this.vars = new HashMap();
        this.left = left;
        this.right = right;
    }

    public Equation(HashMap<Variable, Const> vars, Expression left, Expression right) {
        this.vars = new HashMap(vars);
        this.left = left;
        this.right = right;
    }

    public Equation mod10() {
        return new Equation(left.mod10(), right.mod10());
    }

    @Override
    public String toString() {
        String r = left + " = " + right;
        if (!vars.isEmpty()) {
            r += " // ";
            for (Variable v : vars.keySet())
                r += v + " = " + vars.get(v);
        }
        return r;
    }

}
