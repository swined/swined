package fac;

public class FactorizationTask {

    private final BigInt head;
    private final BigInt tail1;
    private final BigInt tail2;

    public FactorizationTask(BigInt head, BigInt tail1, BigInt tail2) {
        this.head = head;
        this.tail1 = tail1;
        this.tail2 = tail2;
    }

    public BigInt getHead() {
        return head;
    }

    public BigInt getTail1() {
        return tail1;
    }

    public BigInt getTail2() {
        return tail2;
    }

    public String toString() {
        return head.toString() + " " + tail1.toString() + " " + tail2.toString();
    }

}
