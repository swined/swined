package mds1;

import java.util.HashMap;

public class Main {

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
            eq = new Or1(eq, out[i].xor(to[i]).equation());
        return new Not1(eq);
    }

    private static IExp1 optimize(IExp1 exp) {
        HashMap<IExp1, IExp1> context = new HashMap();
        return exp.optimize(context);
    }

    private static Exp32[] xpr(int n) {
        Exp32[] in = new Exp32[n];
        for (int i = 0; i < in.length; i++)
            in[i] = new Exp32("x" + i);
        return in;
    }

    public static void main(String[] args) throws Exception {
        /*Exp32[] to = new Exp32[] {
                new Exp32(0xcd6b8f09L),
                new Exp32(0x73d32146L),
                new Exp32(0x834edecaL),
                new Exp32(0xf6b42726L)
        };*/
        Exp32[] to = new Exp32[] {
            new Exp32(0xd98c1dd4L),
            new Exp32(0x04b2008fL),
            new Exp32(0x980980e9L),
            new Exp32(0x7e42f8ecL),
        };
        Exp32[] in = xpr(0);
        System.out.println(optimize(equation(in, to)));
    }
}
