package jev;

public class Main {

    public static void main(String[] args) {
        EventDispatcher d = new EventDispatcher();
        I i = d.invoke(I.class);
        if (i == null)
            System.out.println("null j");
    }

}
