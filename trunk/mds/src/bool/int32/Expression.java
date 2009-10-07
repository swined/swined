package bool.int32;

public interface Expression {

    Expression rotate(int rotate);
    Expression invert();
    Expression optimize();
    boolean isFalse();
    boolean isTrue();

}
