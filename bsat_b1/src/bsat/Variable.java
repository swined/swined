package bsat;

import java.util.LinkedList;
import java.util.List;

public class Variable implements Expression {

    private String name;
    private boolean negative = false;

    public Variable(String name) {
        this.name = name;
    }

    public Variable(String name, boolean negative) {
        this.name = name;
        this.negative = negative;
    }

    public String getName() {
        return name;
    }

    public boolean isNegative() {
        return negative;
    }

    public String toString() {
        return negative ? "!" + name : name;
    }

    public boolean isComplex() {
        return false;
    }

    public Expression negate() {
        return new Variable(name, !negative);
    }

    public List<List<Variable>> disjunctionalForm() {
        List<Variable> disjunction = new LinkedList<Variable>();
        disjunction.add(new Variable(name, negative));
        List<List<Variable>> conjunction = new LinkedList<List<Variable>>();
        conjunction.add(disjunction);
        return conjunction;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Variable))
            return false;
        Variable variable = (Variable)object;
        if (!name.equals(variable.getName()))
            return false;
        if (negative != variable.isNegative())
            return false;
        return true;
    }

}
