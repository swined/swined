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

    public AVLNode remove(int v) {
        if (value == v) {
            if (height(this) == 1)
                return null;
            throw new RuntimeException("shit happened");
        }
        if (left != null && v < value)
            return new AVLNode(value, left.remove(v), right);
        if (right != null && v > value)
            return new AVLNode(value, left, right.remove(v));
        return this;
    }

    public AVLNode rebalance() {
        if (left != null) {
            AVLNode n = left.rebalance();
            if (n != left)
                return new AVLNode(value, n, right);
        }
        if (right != null) {
            AVLNode n = right.rebalance();
            if (n != right)
                return new AVLNode(value, left, n);
        }
        if (Math.abs(balance(this)) < 2)
            return this;
        if (balance(this) == -2) {
            if (balance(left) == 1)
                return doubleLeftRotate();
            else
                return singleLeftRotate();
        }
        if (balance(this) == 2) {
            if (balance(right) == -1)
                return doubleRightRotate();
            else
                return singleRightRotate();
        }
        throw new RuntimeException("bad balance : " + balance(this));
    }


    public AVLNode doubleRightRotate() {
        AVLNode r = new AVLNode(right.value, right.left.right, right.right);
        AVLNode l = new AVLNode(value, left, right.left.left);
        AVLNode res = new AVLNode(right.left.value, l, r);
        System.out.println("двойной правый поворот (" + value + "): ");
  //      System.out.println("\\Tree " + this);
        //System.out.println("$\\rightarrow$");
    //    System.out.println("\\Tree " + res);
      //  System.out.println();
        return res;
    }

    public AVLNode singleRightRotate() {
        AVLNode l = new AVLNode(value, left, right.left);
        AVLNode res = new AVLNode(right.value, l, right.right);
        System.out.println("простой правый поворот (" + value + "): ");
//        System.out.println("\\Tree " + this);
        //System.out.println("$\\rightarrow$");
  //      System.out.println("\\Tree " + res);
    //    System.out.println();
        return res;
    }

    public AVLNode doubleLeftRotate() {
        AVLNode r = new AVLNode(value, left.right.right, right);
        AVLNode l = new AVLNode(left.value, left.left, left.right.left);
        AVLNode res = new AVLNode(left.right.value, l, r);
        System.out.println("двойной левый поворот (" + value + "): ");
//        System.out.println("\\Tree " + this);
        //System.out.println("$\\rightarrow$");
  //      System.out.println("\\Tree " + res);
    //    System.out.println();
        return res;
    }

    public AVLNode singleLeftRotate() {
        AVLNode r = new AVLNode(value, left.right, right);
        AVLNode res = new AVLNode(left.value, left.left, r);
        System.out.println("простой левый поворот (" + value + "): ");
//        System.out.println("\\Tree " + this);
        //System.out.println("$\\rightarrow$");
//        System.out.println("\\Tree " + res);
  //      System.out.println();
        return res;
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


    public static AVLNode insert(AVLNode n, int v) {
        if (n == null)
            return new AVLNode(v);
        AVLNode r = null;
        if (v < n.value)
            r = new AVLNode(n.value, insert(n.left, v), n.right);
        else
            r = new AVLNode(n.value, n.left, insert(n.right, v));
        //r = r.rebalance();
        return r;
    }

    @Override
    public String toString() {
        if (left == null) {
            if (right == null)
                return "[." + value + " ]";
            else
                return "[." + value + " { } " + right + " ]";
        } else {
            if (right == null)
                return "[." + value + " " + left + " { } ]";
            else
                return "[." + value + " " + left + " " + right + " ]";
        }
    }

}
