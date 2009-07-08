package bsat;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        Parser parser = new Parser();
        Expression expr = parser.parse("!!!x&!(yx|z&c&!yx)|x&y&!yx&!!!x&!(yx|z&c&!yx)|x&y&!yx");
        System.out.println(expr);
        List<List<Variable>> c = expr.disjunctionalForm();
        for (List<Variable> d : c) {
            for (Variable v : d) {
                System.out.print(v);
                if (d.get(d.size() - 1) != v)
                    System.out.print(" & ");
            }
            if (c.get(c.size() - 1) != d)
                System.out.print(" | ");
        }
        System.out.println();
    }

}
