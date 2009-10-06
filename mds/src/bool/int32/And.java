package bool.int32;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class And implements Expression {

    private final Expression a;
    private final Expression b;

    public And(Expression a, Expression b) {
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
        return new And(a.rotate(s), b.rotate(s));
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
            return Const.create(new BitSet());
        if (ob.isFalse())
            return Const.create(new BitSet());
        if (oa.isTrue())
            return ob;
        if (ob.isTrue())
            return oa;
        if (oa instanceof Const && ob instanceof Const)
            return ((Const)oa).and((Const)ob);
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
        if (oa instanceof SimpleDisjunction && ob instanceof SimpleDisjunction) {
            List<SimpleDisjunction> sdnf = new ArrayList();
            sdnf.add((SimpleDisjunction)oa);
            sdnf.add((SimpleDisjunction)ob);
            return new SDNF(sdnf);
        }
        if (oa instanceof SDNF && ob instanceof SimpleDisjunction) {
            List<SimpleDisjunction> sdnf = new ArrayList();
            sdnf.addAll(((SDNF)oa).getItems());
            sdnf.add((SimpleDisjunction)ob);
            return new SDNF(sdnf);
        }
        return new And(oa, ob);
    }
    

}
