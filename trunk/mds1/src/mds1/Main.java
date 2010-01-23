package mds1;

public class Main {

    public static void main(String[] args) throws Exception {
        Exp32 in[] = new Exp32[13];
        for (int i = 0; i < in.length; i++)
            in[i] = new Exp32("x" + i);
        Exp32 out[] = MD5.transform(in);
    }
}
