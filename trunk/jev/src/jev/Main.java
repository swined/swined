package jev;

public class Main implements Runnable {

    public void run() {
        System.out.println("hello, world!");
    }

    public static void main(String[] args) {
        EventDispatcher d = new EventDispatcher();
        d.register(new Main());
        d.register(new Main());
        d.register(new X());
        d.invoke(Runnable.class).run();
    }

}
