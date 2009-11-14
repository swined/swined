package fac;

public class Main {

    public static void main(String[] args) {
        byte[] bytes = {29, 42};
        FactorizationTask t = new FactorizationTask(new BigInt(bytes), new BigInt(), new BigInt());
        System.out.println(t);
    }

}
