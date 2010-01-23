package mds1;

public class Main {

    private static IExp1 split(IExp1 exp, Var1 var) {
        IExp1 p = exp.sub(var, Const1.create(true));
        IExp1 n = exp.sub(var, Const1.create(false));
        return var.and(p).or(var.not().and(n));
    }

    public static void main(String[] args) throws Exception {
        Exp32 in[] = new Exp32[1];
        for (int i = 0; i < in.length; i++)
            in[i] = new Exp32("x" + i);
        Exp32 out[] = MD5.transform(in);
        IExp1 eq = Const1.create(false);
        for (int i = 0; i < out.length; i++)
            eq = eq.or(out[i].equation());
        for (int j = 0; j < in.length; j++)
            for (int i = 0; i < 32; i++)
                eq = split(eq, new Var1("x"+j+"[" + i + "]"));
        //System.out.println(eq);
    }
}
