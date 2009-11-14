package fac;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        int cv[] = { 0x42, 0x17 };
        ConstExpression c = ConstExpression.constExpression(cv);
        Expression x = Expression.variableExpression("x", cv.length);
        Expression y = Expression.variableExpression("y", cv.length);
        List<Equation> queue = new ArrayList();
        queue.add(new Equation(x.multiply(y), c));
        while (!queue.isEmpty()) {
            Equation eq = queue.remove(0);
            if (eq.isSolution())
                System.out.println(eq);
            queue.addAll(eq.solve());
        }
    }

}
