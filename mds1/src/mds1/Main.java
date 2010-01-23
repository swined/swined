package mds1;

public class Main {

    public static void main(String[] args) {
        //byte t[] = "test".getBytes();
        //Exp32 in[] = new Exp32[] { new Exp32(t[0] + (t[1] << 8) + (t[2] << 16) + (t[3] << 24)) };
        Exp32 in[] = new Exp32[13];
        for (int i = 0; i < 13; i++)
            in[i] = new Exp32("x" + i);
        Exp32 out[] = MD5.transform(in);
//        for (int i = 0; i < out.length; i++)
  //          System.out.println(out[i]);
    }
}
