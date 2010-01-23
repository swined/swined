package mds1;

public class PDNF {

    private final boolean c;
    private final Var1 v[][];
    private final static Var1 nothing[] = new Var1[0];

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
        System.gc();
    }

    private static Var1[][] merge(Var1[][] a, Var1[][] b) {
        Var1 r[][] = new Var1[a.length + b.length][0];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    private static Var1[] merge(Var1[] a, Var1[] b) {
        int dupes = 0;
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b.length; j++)
                if (a[i].getName().equals(b[j].getName())) {
                    if (a[i].isInverted() != b[j].isInverted())
                        return nothing;
                    else
                        dupes++;
                }
        Var1 r[] = new Var1[a.length + b.length - dupes];
        System.arraycopy(a, 0, r, 0, a.length);
        if (dupes == 0) {
            System.arraycopy(b, 0, r, a.length, b.length);
        } else {
            int c = 0;
            for (int i = 0; i < a.length; i++)
                for (int j = 0; j < b.length; i++)
                    if (!a[i].equals(b[j])) {
                        r[a.length + c] = b[j];
                        c++;
                    }
        }
        return r;
    }

    public PDNF and(PDNF pdnf) {
        if (!this.c)
            return this;
        Var1 r[][] = new Var1[this.v.length * pdnf.v.length][0];
        for (int i = 0; i < this.v.length; i++)
            for (int j = 0; j < pdnf.v.length; j++)
                r[i*j] = merge(this.v[i], pdnf.v[j]);
        return new PDNF(true, r);
    }

    public PDNF or(PDNF pdnf) {
        if (!this.c)
            return pdnf;
        return new PDNF(true, merge(this.v, pdnf.v));
    }

}
