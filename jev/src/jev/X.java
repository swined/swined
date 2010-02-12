package jev;

public class X implements I {

        public void foo(int x) throws E {
            System.out.println("hello, world");
            throw new E();
        }

        public int bar(int x) {
            return x;
        }

}
