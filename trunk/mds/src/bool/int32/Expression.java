package bool.int32;

public interface Expression {

    Expression invert();
    Expression rotate(int rotate);
    Expression optimize();
    SCNF toSCNF();
    boolean isZero();

}
