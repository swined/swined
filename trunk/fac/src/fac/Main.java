package fac;

import java.util.ArrayList;
import java.util.List;
import util.MutabilityChecker;

public class Main {

    public static void main(String[] args) throws Exception {
        //MutabilityChecker.check(ImmutableMap.class);
        MutabilityChecker.check(Const.class);
//        MutabilityChecker.check(ConstExpression.class);
//        MutabilityChecker.check(Equation.class);
//        MutabilityChecker.check(Expression.class);
//        MutabilityChecker.check(Mod10Equation.class);
//        MutabilityChecker.check(Mod10Expression.class);
//        MutabilityChecker.check(Multiplication.class);
//        MutabilityChecker.check(Tuple.class);
        MutabilityChecker.check(Variable.class);
        int cv[] = { 0x42, 0x00, 0x00, 0x00 };
        ConstExpression c = ConstExpression.constExpression(cv);
        Expression x = Expression.variableExpression("x", cv.length);
        Expression y = Expression.variableExpression("y", cv.length);
        List<Equation> queue = new ArrayList();
        queue.add(new Equation(x.multiply(y), c));
        Equation solution = null;
        while (!queue.isEmpty() && solution == null) {
            Equation eq = queue.remove(queue.size() - 1);
            //System.out.println(eq);
            for (Equation e : eq.solve())
                if (e.isSolution()) {
                    solution = e;
                    continue;
                } else
                    queue.add(e);
        }
        System.out.println();
        if (solution == null)
            System.out.println("solution not found");
        else
            System.out.println(solution);
    }

}
