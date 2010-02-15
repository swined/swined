package jev;

public class Main {

    public static void main(String[] args) throws E {
        EventDispatcher d = new EventDispatcher();
        J j = d.invoke(J.class);
        if (j == null)
            System.out.println("null j");
        j.bar(42);
        j.baz();
    }

}
