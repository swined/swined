package mds1;

public interface IExp1 {

    IExp1 and(IExp1 exp);
    IExp1 or(IExp1 exp);
    IExp1 xor(IExp1 exp);
    IExp1 not();

}
