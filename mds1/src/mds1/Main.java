package mds1;

public class Main {

    private final static long start = System.currentTimeMillis();
    private final static long timeout = 500;

    public static boolean timeout() {
        return System.currentTimeMillis() - start > timeout;
    }

    public static void main(String[] args) throws Exception {
        Exp32 in[] = new Exp32[13];
        for (int i = 0; i < in.length; i++)
            in[i] = new Exp32("x" + i);
        Exp32 out[] = MD5.transform(in);
        if (in.length == 0)
            for (int i = 0; i < out.length; i++)
                System.out.println(out[i]);
        IExp1 eq = Const1.create(false);
        for (int i = 0; i < out.length; i++)
            eq = eq.or(out[i].equation());
        eq.toPDNF();
        //for (int i = 0; i < 31; i++)
          //  eq = eq.substitute(new Var1("x0[" + i + "]"), Const1.create(false));
        //System.out.println(eq);
    }
}
