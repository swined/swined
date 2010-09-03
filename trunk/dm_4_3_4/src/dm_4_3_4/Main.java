/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dm_4_3_4;

/**
 *
 * @author sw
 */
public class Main {

    public static String tree(String m, String[] c) {
        if (m.isEmpty())
            return " ";
        String tree = "";
        for (String s : c)
            if (m.startsWith(s))
                tree += "[.$" + s + "$ " + tree(m.substring(s.length()), c) + "] ";
        if (tree.isEmpty())
            return "\\sout{$" +m+ "$} ";
        return tree;
    }

    public static void main(String[] args) {
        String[] ms = { "ccabccabccabcc", "bccaccabccabccacabcca", "abbccaccabccaabab" };
        for (String m : ms) {
            System.out.println("\\Tree [.$" +m+ "$ " + tree(m, new String[] { "aa","ab","cc","cca","bcca" } ) + "]");
            System.out.println();
        }
    }

}
