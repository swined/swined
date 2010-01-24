package mds1;

import java.math.BigInteger;
import java.util.HashMap;

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

    private static IExp1 split(IExp1 exp) {
        if (!exp.hasDisjunctions())
            return exp;
        Var1 var = exp.getVar();
        if (var == null)
            return exp;
        System.out.println("splitting by " + var);
        IExp1 p = sub(exp, var, true);
        p = split(p);
        IExp1 n = sub(exp, var, false);
        n = split(n);
        return var.and(p).or(var.not().and(n));
    }

    private static IExp1 split(IExp1 exp, Exp32[] in) {
        if (!exp.hasDisjunctions())
            return exp;
        Var1 var = mostUsed(exp, in);
        if (var == null)
            return exp;
        System.out.println("splitting by " + var);
        IExp1 p = sub(exp, var, true);
        p = split(p, in);
        IExp1 n = sub(exp, var, false);
        n = split(n, in);
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

    private static Var1 mostUsed(IExp1 exp, Exp32[] in) {
            Var1 bv = null;
            BigInteger bd = BigInteger.ZERO;
            for (int i = 0; i < in.length; i++)
                for (int j = 0; j < 32; j++) {
                    Var1 v = new Var1("x" + i + "[" + j + "]");
                    HashMap<IExp1, BigInteger> dc = new HashMap();
                    BigInteger depends = exp.depends(dc, v);
                    if (depends.compareTo(bd) == 1) {
                        bv = v;
                        bd = depends;
                    }
                }
            if (bd.compareTo(BigInteger.ZERO) == 0)
                return null;
            else
                return bv;
    }

    public static BigInteger complexity(IExp1 exp) {
            BigInteger bd = BigInteger.ZERO;
            for (int i = 0; i < 1; i++)
                for (int j = 0; j < 32; j++) {
                    Var1 v = new Var1("x" + i + "[" + j + "]");
                    HashMap<IExp1, BigInteger> dc = new HashMap();
                    BigInteger depends = exp.depends(dc, v);
                    bd = bd.add(depends);
                }
            return bd;
    }

    public static void main(String[] args) throws Exception {
        Exp32[] to = new Exp32[] {
                new Exp32(0xcd6b8f09L),
                new Exp32(0x73d32146L),
                new Exp32(0x834edecaL),
                new Exp32(0xf6b42726L)
        };
        Exp32[] in = xpr(1);
        IExp1 eq = equation(in, to);
        System.out.println(complexity(eq));
        eq = split(eq, in);
        System.out.println("done");
        System.out.println(complexity(eq));
        eq.print(System.out);
    }
}
