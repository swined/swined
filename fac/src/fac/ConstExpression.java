package fac;

import util.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstExpression {

    private final ImmutableMap<Integer, Const> muls;

    public ConstExpression(ImmutableMap muls) {
        if (muls.isEmpty()) {
            Map<Integer, Const> r = new HashMap();
            r.put(0, Const.create(0));
            this.muls = new ImmutableMap(r);
        } else {
            this.muls = muls;
        }
    }

    public ConstExpression(List<Tuple<Integer, Const>> nmuls) {
        HashMap<Integer, Const> r = new HashMap();
        List<Tuple<Integer, Const>> m = new ArrayList(nmuls);
        while (!m.isEmpty()) {
            Tuple<Integer, Const> t = m.remove(0);
            if (t.getY().isZero())
                continue;
            if (r.containsKey(t.getX())) {
                Const c = r.remove(t.getX());
                Tuple<Const, Const> x = c.multiply(t.getY());
                r.put(t.getX() + 0, x.getX());
                m.add(new Tuple(t.getX() + 1, x.getY()));
            } else {
                r.put(t.getX(), t.getY());
            }
        }
        if (r.isEmpty())
            r.put(0, Const.create(0));
        muls = new ImmutableMap(r);
    }

    public static ConstExpression constExpression(int c[]) {
        HashMap<Integer, Const> muls = new HashMap();
        for (int i = 0; i < c.length; i++)
            muls.put(i, Const.create(c[i]));
        return new ConstExpression(new ImmutableMap(muls));
    }

    public Const mod10() {
        if (muls.get(0) == null)
            return Const.create(0);
        else
            return muls.get(0);
    }

    public ConstExpression div10() {
        HashMap<Integer, Const> r = new HashMap();
        for (Integer k : muls.keySet())
            if (k > 0)
                r.put(k - 1, muls.get(k));
        return new ConstExpression(new ImmutableMap(r));
    }

    public boolean isZero() {
        for (Integer k : muls.keySet())
            if (!muls.get(k).isZero())
                return false;
        return true;
    }

    public List<Tuple<Integer, Multiplication>> getItems() {
        List<Tuple<Integer, Multiplication>> m = new ArrayList();
        for (Integer k : muls.keySet())
            m.add(new Tuple(k, new Multiplication(muls.get(k))));
        return m;
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
