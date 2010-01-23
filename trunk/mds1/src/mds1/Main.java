package mds1;

import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        //byte t[] = "test".getBytes();
        //Exp32 in[] = new Exp32[] { new Exp32(t[0] + (t[1] << 8) + (t[2] << 16) + (t[3] << 24)) };
        Exp32 in[] = new Exp32[13];
        for (int i = 0; i < in.length; i++)
            in[i] = new Exp32("x" + i);
        Exp32 out[] = MD5.transform(in);
//        for (int i = 0; i < out.length; i++)
            //System.out.println(out[i]);
        HashSet<Var1> vars = new HashSet();
        for (int i = 0; i < out.length; i++)
            out[i].getVars(vars);
        for (Var1 var : vars)
            System.out.println(var);
        System.out.println(vars.size());
    }
}
