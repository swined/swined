package fac;

import java.util.HashMap;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws Exception {
        int cv[] = { 0x42, 0x29, 0x54, 0x17 };
        ConstExpression c = ConstExpression.constExpression(cv);
        Expression x = Expression.variableExpression("x", cv.length);
        Expression y = Expression.variableExpression("y", cv.length);
        Equation e = new Equation(x.multiply(y), c);
        System.out.println(e);
        System.out.println(e.mod10());
        System.out.println(e.div10());
        Set<HashMap<Variable, Const>> solutions = e.mod10().solve();
        for (HashMap<Variable, Const> solution : solutions) {
            for (Variable v : solution.keySet())
                System.out.print(v + " = " + solution.get(v) + " ");
            System.out.println();
        }
    }

}
