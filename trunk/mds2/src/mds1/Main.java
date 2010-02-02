package mds1;

import java.util.HashMap;

public class Main {

    private static IExp1 sub(IExp1 e, Var1 v, Const1 c) {
        HashMap<IExp1, IExp1> ctx = new HashMap();
        return e.sub(ctx, v, c);
    }

    private static IExp1 split(IExp1 e, Var1 v) {
        System.out.println("splitting by " + v);
        IExp1 p = sub(e, v, Const1.create(true));
        IExp1 n = sub(e, v, Const1.create(false));
        return new Or1(new And1(v, p), new And1(new Not1(v), n));
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
        /*Exp32[] to = new Exp32[] { // 'test'
                new Exp32(0xcd6b8f09L),
                new Exp32(0x73d32146L),
                new Exp32(0x834edecaL),
                new Exp32(0xf6b42726L)
        };*/
        /*Exp32[] to = new Exp32[] { // ''
            new Exp32(0xd98c1dd4L),
            new Exp32(0x04b2008fL),
            new Exp32(0x980980e9L),
            new Exp32(0x7e42f8ecL),
        };*/
        /*Exp32[] to = new Exp32[] { // '0000'
            new Exp32(0xd41e7d4aL),
            new Exp32(0x404e4714L),
            new Exp32(0xcc29ac33L),
            new Exp32(0x9b3d65b8L),
        };*/
        Exp32[] to = new Exp32[] { // '00001111'
            new Exp32(0x18217255L),
            new Exp32(0x8e64b7e8L),
            new Exp32(0x55b68553L),
            new Exp32(0x1277a728L),
        };
        Exp32[] in = xpr(2);
        Exp32[] inx = new Exp32[] { new Exp32(0x30303030L), new Exp32(0x31313131L) };
        IExp1 eq = optimize(equation(in, to));
//        for (int i = 0; i < 4; i++)
  //          eq = optimize(split(eq, new Var1("x0[" + i + "]")));
        for (int k = 0; k < in.length; k++)
            for (int i = 0; i < 32; i++)
                eq = optimize(sub(eq, new Var1("x" + k + "[" + i + "]"), (Const1)inx[k].bits()[i]));
        System.out.println(eq);
    }
}
