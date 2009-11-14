package fac;

import java.util.HashSet;

public class Multiplication {

    private int power;
    private Const c;
    private HashSet<Variable> vars;

    public Multiplication(int power, Const c, HashSet<Variable> vars) {
        this.power = power;
        this.c = c;
        this.vars = new HashSet(vars);
    }

}
