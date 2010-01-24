package mds1;

import java.util.HashMap;

public class Main {

    private static IExp1 split(IExp1 exp, boolean a) {
        if (!exp.hasDisjunctions())
            return exp;
        Var1 var = !a ? exp.getVarA() : exp.getVarB();
        if (var == null)
            return exp;
        System.out.println("splitting by " + var);
        HashMap<IExp1, IExp1> context = new HashMap();
        IExp1 p = exp.sub(context, var, Const1.create(true));
        if (a)
            p = split(p, a);
        //if (!p.hasDisjunctions())
        //    if (p.getVar() != null)
        //        return var.and(p);
        context = new HashMap();
        IExp1 n = exp.sub(context, var, Const1.create(false));
        if (!a)
            n = split(n, a);
        //if (!n.hasDisjunctions())
        //    if (n.getVar() != null)
        //        return var.not().and(n);
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
        for (int i = 0; i < 2; i++)
            eq = split(eq, i % 2 == 0);
        eq.print(System.out);
        System.out.println();
    }
}
