package fac;

import java.util.HashMap;

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
        return new Equation(left.div10(), right.div10());
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
