package fantom;

import fan.sys.Depend;
import fanx.fcode.FConst;
import fanx.fcode.FMethod;
import fanx.fcode.FPod;
import fanx.fcode.FStore;
import fanx.fcode.FType;
import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        System.setProperty("fan.home", "/home/sw/fantom-1.0.51");
        FPod pod = new FPod("sys", FStore.makeZip(new File("/home/sw/fantom-1.0.51/lib/fan/sys.pod")));
        pod.read();
        System.out.println("deps:");
        for (Depend dep : pod.depends) {
            System.out.println(dep.name());
        }
        System.out.println();
        System.out.println("types:");
        for (FType ftype : pod.types) {
            String typeName = pod.typeRefs.toString(ftype.self);
            String baseName = ftype.base == 65535 ? "<none>" : pod.typeRefs.toString(ftype.base);
            System.out.println(typeName + " : " + baseName);
            if ((ftype.flags & FConst.Abstract) > 0) {
                System.out.println("abstract");
            } if (ftype.methods == null) {
                System.out.println("no meth");
            } else {
                for (FMethod meth : ftype.methods) {
                    System.out.println(meth.name);
                }
            }
        }
    }

}
