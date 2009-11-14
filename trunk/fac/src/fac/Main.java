package fac;

public class Main {

    public static void main(String[] args) throws Exception {
        int cv[] = { 0x42, 0x29, 0x54, 0x17 };
        ConstExpression c = ConstExpression.constExpression(cv);
        Expression x = Expression.variableExpression("x", cv.length);
        Expression y = Expression.variableExpression("y", cv.length);
        Equation e = new Equation(x.multiply(y), c);
        System.out.println(e.div10());
        for (Equation solution : e.solve())
            System.out.println(solution);
    }

}