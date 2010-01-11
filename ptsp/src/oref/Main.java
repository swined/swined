package oref;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main {

    private static BufferedImage draw(Path p, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        for (int i = 0; i < p.getPointCount(); i++) {
            Point p1 = p.getPoint(i);
            Point p2 = p.getPoint((i + 1) % p.getPointCount());
            g.fillRect(
                    (int)(p1.getX() * size) - 5,
                    (int)(p1.getY() * size) - 5,
                    10,
                    10
            );
            g.drawLine(
                    (int)(p1.getX() * size),
                    (int)(p1.getY() * size),
                    (int)(p2.getX() * size),
                    (int)(p2.getY() * size)
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
        int n = 1;
        while (n < path.length / 2 + 1) {
            Path np = p.optimize2(n);
            if (np == null) {
                System.out.println(p.getLength() + " " + n);
                n++;
            } else {
                p = np;
                System.out.println(p.getLength() + " " + n);
                n = 2;
            }
        }
        
//        Path np = null;
  //      while ((np = p.optimize2(1)) != null)
    //        p = np;
        System.out.println(p.getLength());
        //File outputfile = new File("/home/sw/oref.png");
        //ImageIO.write(draw(p), "png", outputfile);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(420, 435);
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
        final Image i = draw(p, 400);
        JPanel panel = new JPanel() {
              public void paint(Graphics g) {
                    g.drawImage( i, 0, 0, null);
              }
        };
        frame.add(panel);
        //frame.show();
        
    }

}
