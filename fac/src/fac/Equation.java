package fac;

import java.util.HashMap;

public class Equation {

    private final HashMap<Variable, Const> vars;
    private final Expression left;
    private final Expression right;

    public Equation(HashMap<Variable, Const> vars, Expression left, Expression right) {
        this.vars = new HashMap(vars);
        this.left = left;
        this.right = right;
    }

}
