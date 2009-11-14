package fac;

import java.util.HashMap;
import java.util.List;

public class Mod10Equation {

    private final Mod10Expression left;
    private final Const right;

    public Mod10Equation(Mod10Expression left, Const right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left + " = " + right;
    }

    public HashMap<Variable, Const> solve() throws Exception {
        List<Multiplication> muls = left.getMultiplications();
        if (muls.size() == 1) {
            Multiplication m = muls.get(0);
            List<Variable> vars = m.getVars();
            if (vars.size() == 2) {
                Variable v1 = vars.get(0);
                Variable v2 = vars.get(1);
                HashMap<Variable, Const> r = new HashMap();
                for (Tuple<Const, Const> pair : right.demultiplyMod10()) {
                    r.put(v1, pair.getX());
                    r.put(v2, pair.getY());
                }
                return r;
            }
        }
        throw new Exception("don't know how to solve " + this);
    }

}
