package fac;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<HashMap<Variable, Const>> generateSolutions(Variable v1, Variable v2, List<Tuple<Const, Const>> solutions) {
        Set<HashMap<Variable, Const>> r = new HashSet();
        for (Tuple<Const, Const> pair : right.demultiplyMod10()) {
            HashMap<Variable, Const> t = new HashMap();
            t.put(v1, pair.getX());
            t.put(v2, pair.getY());
            r.add(t);
        }
        return r;
    }

    public Set<HashMap<Variable, Const>> solve() throws Exception {
        List<Multiplication> muls = left.getMultiplications();
        if (muls.size() == 1) {
            Multiplication m = muls.get(0);
            List<Variable> vars = m.getVars();
            if (vars.size() == 0) {
                Const c = m.getConst();
                Set<HashMap<Variable, Const>> r = new HashSet();
                if (c == right) {
                    r.add(new HashMap());
                }
                return r;
            }
            if (vars.size() == 2) {
                return generateSolutions(vars.get(0), vars.get(1), right.demultiplyMod10());
            }
        }
        if (muls.size() == 2) {
            if (muls.get(0).getVars().size() == 1) {
                Const c1 = muls.get(0).getConst();
                Variable v1 = muls.get(0).getVars().get(0);
                if (muls.get(1).getVars().size() == 1) {
                    Const c2 = muls.get(1).getConst();
                    Variable v2 = muls.get(1).getVars().get(0);
                    return generateSolutions(v1, v2, right.solveLinear(Const.create(0), c1, c2));
                }
            }
        }
        if (muls.size() == 3) {
            if (muls.get(0).getVars().isEmpty()) {
                Const c0 = muls.get(0).getConst();
                if (muls.get(1).getVars().size() == 1) {
                    Const c1 = muls.get(1).getConst();
                    Variable v1 = muls.get(1).getVars().get(0);
                    if (muls.get(2).getVars().size() == 1) {
                        Const c2 = muls.get(2).getConst();
                        Variable v2 = muls.get(2).getVars().get(0);
                        return generateSolutions(v1, v2, right.solveLinear(c0, c1, c2));
                    }
                }
            }
            if (muls.get(0).getVars().size() == 1) {
                Const c1 = muls.get(0).getConst();
                Variable v1 = muls.get(0).getVars().get(0);
                if (muls.get(1).getVars().size() == 1) {
                    Const c2 = muls.get(1).getConst();
                    Variable v2 = muls.get(1).getVars().get(0);
                    if (muls.get(2).getVars().isEmpty()) {
                        Const c0 = muls.get(2).getConst();
                        return generateSolutions(v1, v2, right.solveLinear(c0, c1, c2));
                    }
                }
            }
        }
        throw new Exception("don't know how to solve " + this);
    }
}
