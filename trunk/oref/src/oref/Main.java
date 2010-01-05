package oref;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        final int c = 100;
        Random rand = new Random();
        Point path[] = new Point[c];
        for (int i = 0; i < c; i++)
            path[i] = new Point(rand.nextDouble(), rand.nextDouble());
        Path p = new Path(path);
        System.out.println(p.getLength());
        Path np = null;
        while ((np = p.optimize()) != null) {
            p = np;
            //System.out.println(p.getLength());
        }
        System.out.println(p.getLength());
    }

}
