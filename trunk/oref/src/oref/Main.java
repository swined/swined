package oref;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;

public class Main {

    private static BufferedImage draw(Path p) {
        BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < p.getPointCount(); i++) {
            Point p1 = p.getPoint(i);
            Point p2 = p.getPoint((i + 1) % p.getPointCount());
            img.getGraphics().drawLine(
                    (int)(p1.getX() * 1000),
                    (int)(p1.getY() * 1000),
                    (int)(p2.getX() * 1000),
                    (int)(p2.getY() * 1000)
            );
        }
        return img;
    }

    public static void main(String[] args) throws Exception {
        final int c = 50;
        Random rand = new Random();
        Point path[] = new Point[c];
        for (int i = 0; i < c; i++)
            path[i] = new Point(rand.nextDouble(), rand.nextDouble());
        Path p = new Path(path);
        System.out.println(p.getLength());
        int n = 2;
        while (n < 5) {
            Path np = p.optimize(n);
            if (np == null) {
                n++;
            } else {
                p = np;
                System.out.println(p.getLength() + " " + n);
                n = 2;
            }
        }
        System.out.println(p.getLength());
        File outputfile = new File("/home/sw/oref.png");
        ImageIO.write(draw(p), "png", outputfile);
    }

}
