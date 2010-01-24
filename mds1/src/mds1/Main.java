package mds1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Main {

    private static IExp1 sub(IExp1 e, Var1 v, boolean c) {
        HashMap<IExp1, IExp1> context = new HashMap();
        return e.sub(context, v, Const1.create(c));
    }

    private static IExp1 split(IExp1 exp, Var1 var) {
        System.out.println("splitting by " + var);
        IExp1 p = sub(exp, var, true);
        IExp1 n = sub(exp, var, false);
        return var.and(p).or(var.not().and(n));
    }

    private static IExp1 split(IExp1 exp, List<Var1> vars) {
        if (!exp.hasDisjunctions())
            return exp;
        Var1 var = exp.getVar();
        vars.add(var);
        if (var == null)
            return exp;
        System.out.println("splitting by " + var);
        IExp1 p = sub(exp, var, true);
        //p = split(p, vars);
        IExp1 n = sub(exp, var, false);
        n = split(n, vars);
        return var.and(p).or(var.not().and(n));
    }

    private static IExp1 equation(Exp32[] in, Exp32[] to) {
        Exp32 out[] = MD5.transform(in);
        for (int i = 0; i < out.length; i++)
            if (out[i].asLong() == null) {
                System.out.println("*");
            } else {
                System.out.println("0x" + Long.toHexString(out[i].asLong()));
            }
        IExp1 eq = Const1.create(false);
        for (int i = 0; i < out.length; i++)
            eq = eq.or(out[i].xor(to[i]).equation());
        return eq.not();
    }

    private static Exp32[] xpr(int n) {
        Exp32[] in = new Exp32[n];
        for (int i = 0; i < in.length; i++)
            in[i] = new Exp32("x" + i);
        return in;
    }

    public static void main(String[] args) throws Exception {
        Exp32[] to = new Exp32[] {
                new Exp32(0xcd6b8f09L),
                new Exp32(0x73d32146L),
                new Exp32(0x834edecaL),
                new Exp32(0xf6b42726L)
        };
        IExp1 eq = equation(xpr(1), to);
        List<Var1> vars = new LinkedList();
        for (int i = 0; i < 3; i++) {
            eq = split(eq, vars);
            eq = split(eq, vars.get(vars.size() - 1));
            vars.clear();
        }
        System.out.println("done");
        eq.print(System.out);
        System.out.println();
    }
}
