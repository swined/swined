package jev;

public class X implements Runnable, I {

        public void run() {
            System.out.println("bye, world!");
        }

        public void foo(int x) throws IllegalArgumentException {
            throw new IllegalArgumentException("shit happened");
        }

}
