package bsat;

import java.util.List;

public interface Expression {

    boolean isComplex();
    Expression negate();
    List<List<Variable>> disjunctionalForm();

}
