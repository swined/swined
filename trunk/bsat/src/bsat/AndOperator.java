package bsat;

import java.util.LinkedList;
import java.util.List;

public class AndOperator implements Expression {

    private Expression arg1;
    private Expression arg2;

    public AndOperator(Expression arg1, Expression arg2) {
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
        String a1 = arg1.isComplex() ? "(" + arg1.toString() + ")" : arg1.toString();
        String a2 = arg2.isComplex() ? "(" + arg2.toString() + ")" : arg2.toString();
        return a1 + " & " + a2;
    }

    public boolean isComplex() {
        return true;
    }

    public Expression negate() {
        return new OrOperator(arg1.negate(), arg2.negate());
    }

    public List<List<Variable>> disjunctionalForm() {
        List<List<Variable>> r = new LinkedList<List<Variable>>();
        List<List<Variable>> a1 = arg1.disjunctionalForm();
        List<List<Variable>> a2 = arg2.disjunctionalForm();
        for (List<Variable> d1 : a1) {
            for (List<Variable> d2 : a2) {
                List<Variable> d0 = new LinkedList<Variable>();
                d0.addAll(d1);
                d0.addAll(d2);
                r.add(d0);
            }
        }
        return r;
    }

}
