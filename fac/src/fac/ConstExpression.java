package fac;

import java.util.ArrayList;
import java.util.List;

public class ConstExpression {

    private final List<Tuple<Integer, Const>> muls;

    public ConstExpression(List<Tuple<Integer, Const>> muls) {
        this.muls = new ArrayList(muls);
    }

    public ConstExpression mod10() {
        List<Tuple<Integer, Const>> r = new ArrayList();
        for (Tuple<Integer, Const> m : muls)
            if (m.getX() == 0)
                r.add(m);
        return new ConstExpression(r);
    }

    @Override
    public String toString() {
        String r = "";
        for (Tuple<Integer, Const> m : muls) {
            if (!r.isEmpty())
                r += " + ";
            if (m.getX() != 0)
                r += "10^" + m.getX() + "*";
            r += m.getY();
        }
        return r;
    }

}
