package mds1;

public class Main {

    private static IExp1 split(IExp1 exp) {
        if (!exp.hasDisjunctions())
            return exp;
        Var1 var = exp.getVar();
        if (var == null)
            return exp;
        System.out.println("splitting by " + var);
        IExp1 p = split(exp.sub(var, Const1.create(true)));
        IExp1 n = split(exp.sub(var, Const1.create(false)));
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
        Exp32[] in = new Exp32[1];
        for (int i = 0; i < in.length; i++)
            in[i] = new Exp32("x" + i);
        return in;
    }

    public static void main(String[] args) throws Exception {
        Exp32[] in = xpr(1);
        byte[] t = "test".getBytes();
        Exp32[] ti = new Exp32[] {
                new Exp32(t[0] | t[1] << 8 | t[2] << 16 | t[3] << 24)
        };
        Exp32[] to = new Exp32[] {
                new Exp32(0xcd6b8f09L),
                new Exp32(0x73d32146L),
                new Exp32(0x834edecaL),
                new Exp32(0xf6b42726L)
        };
        IExp1 eq = equation(in, to);
        int d = 19;
        for (int j = 0; j < in.length; j++)
            for (int i = 0; i < d; i++)
                eq = eq.sub(new Var1("x"+j+"[" + i + "]"), (Const1)(ti[0].bits()[i]));
        System.out.println(split(eq));
    }
}
