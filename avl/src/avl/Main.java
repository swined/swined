package avl;

public class Main {

    public static void main(String[] args) {
        int[] data = new int[] { 29, 19, 9, 1, 4, 14, 39, 18, 36, 24, 15, 12 };
        AVLNode node = null;
        for (int d : data) {
            node = AVLNode.insert(node, d, "");
            System.out.println("-- " + node);
        }
    }

}
