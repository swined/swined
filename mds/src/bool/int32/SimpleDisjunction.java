package bool.int32;

public class SimpleDisjunction implements Expression {

    private Const coef;
    private Variable[] vars;

    public SimpleDisjunction rotate(int s) {
        Const rc = coef.rotate(s);
        Variable v[] = new Variable[vars.length];
        for (int i = 0; i < vars.length; i++)
            v[i] = vars[i].rotate(s);
        return new SimpleDisjunction(rc, v);
    }

    public SimpleDisjunction(Const c) {
        coef = c;
        vars = new Variable[0];
        checkConst();
    }

    public SimpleDisjunction(Variable e) {
        coef = Const.FALSE();
        vars = new Variable[1];
        vars[0] = e;
        checkConst();
    }

    public SimpleDisjunction(Const c, Variable e) {
        coef = c;
        vars = new Variable[1];
        vars[0] = e;
        checkConst();
    }

    public SimpleDisjunction(Variable v1, Variable v2) {
        coef = Const.FALSE();
        vars = new Variable[2];
        vars[0] = v1;
        vars[1] = v2;
        checkConst();
    }

    public SimpleDisjunction(Const c, Variable e[]) {
        coef = c;
        vars = e;
        checkConst();
    }

    public SimpleDisjunction(SimpleDisjunction a, SimpleDisjunction b) {
        coef = a.getCoef().or(b.getCoef());
        Variable va[] = a.getVars();
        Variable vb[] = b.getVars();
        vars = new Variable[va.length + vb.length];
        for (int i = 0; i < va.length; i++)
            vars[i] = va[i];
        for (int i = 0; i < vb.length; i++)
            vars[i + va.length] = vb[i];
        checkConst();
    }

    public Const getCoef() {
        return coef;
    }

    public Variable[] getVars() {
        return vars;
    }

    public boolean isFalse() {
        return vars.length == 0 && coef.isFalse();
    }

    public void checkConst() {
        if (isFalse()) {
            coef = Const.FALSE();
            vars = new Variable[0];
        }
        if (isTrue()) {
            coef = Const.TRUE();
            vars = new Variable[0];
        }
    }

    public boolean isTrue() {
        if (coef.isTrue())
            return true;
        for (Variable v1 : vars)
            for (Variable v2 : vars)
                if (v1.invert().equals(v2))
                    return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder r = new StringBuilder();
        r.append("{");
        r.append(coef);
        for (Variable v : vars) {
            r.append(" | ");
            r.append(v);
        }
        r.append("}");
        return r.toString();
    }

    public SimpleConjunction invert() {
        Variable v[] = new Variable[vars.length];
        for (int i = 0; i < vars.length; i++)
            v[i] = vars[i].invert();
        return new SimpleConjunction(coef.invert(), v);
    }

    public Expression optimize() {
        if (vars.length == 0)
            return coef;
        return this;
    }

}