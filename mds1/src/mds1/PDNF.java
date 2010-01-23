package mds1;

public class PDNF implements IExp1 {

    private final boolean c;
    private final Var1 v[][];

    public PDNF(boolean c) {
        this.c = c;
        this.v = new Var1[0][0];
    }

    public PDNF(Var1 v) {
        this.c = true;
        this.v = new Var1[][] { new Var1[] { v } };
    }

    private PDNF(boolean c, Var1 v[][]) {
        this.c = c;
        this.v = v;
    }

    private static Var1[][] merge(Var1[][] a, Var1[][] b) {
        Var1 r[][] = new Var1[0][a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    private static Var1[] merge(Var1[] a, Var1[] b) {
        Var1 r[] = new Var1[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public PDNF and(IExp1 exp) {
        if (!this.c)
            return this;
        PDNF pdnf = exp.toPDNF();
        Var1 r[][] = new Var1[0][this.v.length * pdnf.v.length];
        for (int i = 0; i < this.v.length; i++)
            for (int j = 0; j < pdnf.v.length; j++)
                r[i*j] = merge(this.v[i], pdnf.v[j]);
        return new PDNF(true, r);
    }

    public IExp1 or(IExp1 pdnf) {
        if (!this.c)
            return pdnf;
        return new PDNF(true, merge(this.v, pdnf.toPDNF().v));
    }

    public IExp1 not() {
        return new Not1(this);
    }

    public IExp1 xor(IExp1 exp) {
        return exp.not().and(this).or(this.not().and(exp));
    }

    public PDNF toPDNF() {
        return this;
    }

    public IExp1 substitute(Var1 v, Const1 c) {
        throw new UnsupportedOperationException();
    }

}
