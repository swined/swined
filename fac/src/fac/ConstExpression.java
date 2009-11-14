package fac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConstExpression {

    private final HashMap<Integer, Const> muls = new HashMap();

    public ConstExpression(List<Tuple<Integer, Const>> nmuls) {
        List<Tuple<Integer, Const>> m = new ArrayList(nmuls);
        while (!m.isEmpty()) {
            Tuple<Integer, Const> t = m.remove(0);
            if (muls.containsKey(t.getX())) {
                Const c = muls.remove(t.getX());
                Tuple<Const, Const> x = c.multiply(t.getY());
                m.add(new Tuple(0, x.getX()));
                m.add(new Tuple(1, x.getY()));
            }
        }
        if (muls.isEmpty())
            muls.put(0, new Const(0));
    }

    public static ConstExpression constExpression(int c[]) {
        List<Tuple<Integer, Const>> muls = new ArrayList();
        for (int i = 0; i < c.length; i++)
            muls.add(new Tuple<Integer, Const>(i, new Const(c[i])));
        return new ConstExpression(muls);
    }

    public Const mod10() {
        if (muls.get(0) == null)
            return new Const(0);
        else
            return muls.get(0);
    }

    public ConstExpression div10() {
        List<Tuple<Integer, Const>> r = new ArrayList();
        for (Integer k : muls.keySet())
            if (k > 0)
                r.add(new Tuple(k - 1, muls.get(k)));
        return new ConstExpression(r);
    }

    public boolean isZero() {
        for (Integer k : muls.keySet())
            if (!muls.get(k).isZero())
                return false;
        return true;
    }

    @Override
    public String toString() {
        String r = "";
        for (Integer k : muls.keySet()) {
            if (!r.isEmpty())
                r += " + ";
            r += new Tuple(k, muls.get(k));
        }
        return r;
    }

}
