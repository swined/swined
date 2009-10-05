package bool.int32;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class And implements Expression {

    private final Expression a;
    private final Expression b;
    private static List<WeakReference<And>> pool = new ArrayList();

    private static void cleanPool() {
        List<WeakReference<And>> p = new ArrayList();
        for (WeakReference<And> r : pool) {
            if (r.get() != null)
                p.add(r);
        }
        pool = p;
    }

    public static And create(Expression a, Expression b) {
        cleanPool();
        for (WeakReference<And> r : pool) {
            And and = r.get();
            if (and != null)
                if (and.getA().equals(a) && and.getB().equals(b))
                    return and;
        }
        And and = new And(a, b);
        pool.add(new WeakReference(and));
        return and;
    }

    private And(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    public Expression getA() {
        return a;
    }

    public Expression getB() {
        return b;
    }

    public boolean isFalse() {
        return a.isFalse() || b.isFalse();
    }

    public boolean isTrue() {
        return a.isTrue() && b.isTrue();
    }

    public Expression rotate(int s) {
        return And.create(a.rotate(s), b.rotate(s));
    }

    @Override
    public String toString() {
        return "(" + a.toString() + " & " + b.toString() + ")";
    }

    public Expression invert() {
        return new Or(a.invert(), b.invert());
    }

    public Expression optimize() {
        final Expression oa = a.optimize();
        final Expression ob = b.optimize();
        if (oa.isFalse())
            return Const.create(0);
        if (ob.isFalse())
            return Const.create(0);
        if (oa.isTrue())
            return ob;
        if (ob.isTrue())
            return oa;
        if (oa instanceof Const && ob instanceof Variable)
            return new SimpleConjunction(new SimpleConjunction((Const)oa), new SimpleConjunction((Variable)ob));
        if (oa instanceof Variable && ob instanceof Const)
            return new SimpleConjunction(new SimpleConjunction((Const)ob), new SimpleConjunction((Variable)oa));
        if (oa instanceof Variable && ob instanceof Variable)
            return new SimpleConjunction(new SimpleConjunction((Variable)ob), new SimpleConjunction((Variable)oa));
        if (oa instanceof Variable && ob instanceof SimpleConjunction)
            return new SimpleConjunction((SimpleConjunction)ob, new SimpleConjunction((Variable)oa));
        if (oa instanceof SimpleConjunction && ob instanceof Const)
            return new SimpleConjunction(new SimpleConjunction((Const)ob), (SimpleConjunction)oa);
        if (oa instanceof SimpleConjunction && ob instanceof SimpleConjunction)
            return new SimpleConjunction((SimpleConjunction)oa, (SimpleConjunction)ob);
        return And.create(oa, ob);
    }
    

}
