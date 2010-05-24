package avl;

public class Main {

    public static void main(String[] args) {
        int[] data = new int[] { 29, 19, 9, 1, 4, 14, 39, 18, 36, 24, 15, 12 };
        AVLNode node = null;
        for (int d : data) {
            System.out.println("вставляем " + d + " : ");
            System.out.println();
            node = AVLNode.insert(node, d);
            System.out.println("\\Tree " + node);
            System.out.println();
            while (true) {
                AVLNode n = node.rebalance();
                if (n == node)
                    break;
                else
                    node = n;
                System.out.println("\\Tree " + node);
                System.out.println();
            }
        }
    }

}
