package avl;

public class AVLNode {

    private final int value;
    private final AVLNode left;
    private final AVLNode right;

    public AVLNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public AVLNode(int value, AVLNode left, AVLNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public static int balance(AVLNode node) {
        if (node == null)
            return 0;
        else
            return height(node.right) - height(node.left);
    }

    public AVLNode rebalance() {
        if (Math.abs(balance(this)) < 2)
            return this;
        if (balance(this) == -2) {
            if (balance(right) == 1)
                return doubleRightRotate();
            else
                return singleRightRotate();
        }
        if (balance(this) == 2) {
            if (balance(right) == -1)
                return doubleLeftRotate();
            else
                return singleLeftRotate();
        }

        System.err.println("shit happened");
        return this;
    }


    public AVLNode doubleRightRotate() {
        System.out.println("double right rotate (" + value + ")");
        AVLNode r = new AVLNode(right.value, right.left.right, right.right);
        AVLNode l = new AVLNode(value, left, right.left.left);
        return new AVLNode(right.left.value, l, r);
    }

    public AVLNode singleRightRotate() {
        System.out.println("single right rotate (" + value + ")");
        AVLNode l = new AVLNode(value, left, right.left);
        return new AVLNode(right.left.value, l, right.right);
    }

    public AVLNode doubleLeftRotate() {
        return this;
    }

    public AVLNode singleLeftRotate() {
        return this;
    }

    public static int height(AVLNode node) {
        if (node == null)
            return 0;
        int r = height(node.right);
        int l = height(node.left);
        if (r > l)
            return r + 1;
        else
            return l + 1;
    }


    public static AVLNode insert(AVLNode n, int v, String p) {
        if (n == null)
            return new AVLNode(v);
        System.out.println(p + "inserting " + v + " into " + n);
        AVLNode r = null;
        if (v < n.value)
            r = new AVLNode(n.value, insert(n.left, v, p + "  "), n.right);
        else
            r = new AVLNode(n.value, n.left, insert(n.right, v, p + "  "));
        System.out.println(p + r);
        return r.rebalance();
    }

    @Override
    public String toString() {
        if (left == null) {
            if (right == null)
                return "" + value;
            else
                return "" + value + "->{ R" + right + " }";
        } else {
            if (right == null)
                return "" + value + "->{ L" + left + " }";
            else
                return "" + value + "->{ " + left + ", " + right + " }";
        }
    }

}
