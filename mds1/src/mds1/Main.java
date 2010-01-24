package mds1;

public class Main {

    private static IExp1 split(IExp1 exp) {
        if (!exp.hasDisjunctions())
            return exp;
        Var1 var = exp.getVar();
        if (var == null)
            return exp;
        System.out.println("splitting by " + var);
        IExp1 p = exp.sub(var, Const1.create(true));
        p = split(p);
        if (!p.hasDisjunctions())
            if (p.getVar() != null)
                return var.and(p);
        IExp1 n = exp.sub(var, Const1.create(false));
        n = split(n);
        if (!n.hasDisjunctions())
            if (n.getVar() != null)
                return var.not().and(n);
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
                new Exp32(0),//xcd6b8f09L),
                new Exp32(0),//x73d32146L),
                new Exp32(0),//x834edecaL),
                new Exp32(0),//xf6b42726L)
        };
        IExp1 eq = equation(xpr(2), to);
        eq = split(eq);
        eq.print(System.out);
        System.out.println();
    }
}
