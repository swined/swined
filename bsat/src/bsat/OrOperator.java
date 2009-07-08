package bsat;

import java.util.LinkedList;
import java.util.List;

public class OrOperator implements Expression {

    private Expression arg1;
    private Expression arg2;

    public OrOperator(Expression arg1, Expression arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public Expression getArg1() {
        return arg1;
    }

    public Expression getArg2() {
        return arg2;
    }

    public String toString() {
        return arg1.toString() + " | " + arg2.toString();
    }

    public boolean isComplex() {
        return true;
    }

    public Expression negate() {
        return new AndOperator(arg1.negate(), arg2.negate());
    }

    public List<List<Variable>> disjunctionalForm() {
        List<List<Variable>> r = new LinkedList<List<Variable>>();
        r.addAll(arg1.disjunctionalForm());
        r.addAll(arg2.disjunctionalForm());
        return r;
    }

}
