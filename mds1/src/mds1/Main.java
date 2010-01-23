package mds1;

public class Main {

    public static void main(String[] args) throws Exception {
        Exp32 in[] = new Exp32[1];
        for (int i = 0; i < in.length; i++)
            in[i] = new Exp32("x" + i);
        for (int i = 0; i < 30; i++)
            in[0].setBit(i, Const1.create(false));
//        for (int i = 31; i < 32; i++)
  //          in[0].setBit(i, Const1.create(false));
        //in[0] = new Exp32(0x80000000L);
        Exp32 out[] = MD5.transform(in);
  //      for (int i = 0; i < out.length; i++)
//            System.out.println(out[i]);
        IExp1 eq = Const1.create(false);
        for (int i = 0; i < out.length; i++)
            eq = eq.or(out[i].equation());
        System.out.println(eq);
//        for (int j = 0; j < in.length; j++)
 //           for (int i = 0; i < 30; i++)
   //             eq = eq.sub(new Var1("x" + j + "[" + i + "]"), Const1.create(false));
     //   System.out.println(eq);
    }
}
