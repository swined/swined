package mds1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
        List<Var1[]> x = new ArrayList();
        for (int i = 0; i < v.length; i++)
            if (v[i].length > 0)
                x.add(v[i]);
        this.v = x.toArray(v);
    }

    private static Var1[][] merge(Var1[][] a, Var1[][] b) {
        Var1 r[][] = new Var1[a.length + b.length][0];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    private static Var1[] merge(Var1[] a, Var1[] b) {
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b.length; j++)
                if (a[i].not().equals(b[j]))
                    return nothing;
        HashSet<Var1> vars = new HashSet();
        for (int i = 0; i < a.length; i++)
            vars.add(a[i]);
        for (int i = 0; i < b.length; i++)
            vars.add(b[i]);
        return vars.toArray(a);
    }

    public PDNF and(PDNF pdnf) {
        if (!this.c)
            return this;
        int l = this.v.length * pdnf.v.length;
        if (l < 0)
            throw new RuntimeException("" + this.v.length + " * " + pdnf.v.length + " = " + l);
        Var1 r[][] = new Var1[l][0];
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
