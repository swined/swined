package bool.int32;

public interface Expression {

    Expression invert();
    Expression optimize();
    Expression rotate(int rotate);
    SCNF toSCNF();

}
