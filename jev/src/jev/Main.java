package jev;

public class Main {

    public static void main(String[] args) {
        EventDispatcher d = new EventDispatcher();
        d.register(new X());
        d.register(new Y());
        I j = d.invoke(J.class);
        j.foo(42);
    }

}
