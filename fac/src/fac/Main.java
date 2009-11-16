package fac;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
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
