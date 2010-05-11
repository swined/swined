package nsuj07;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    
    private static Object invoke(Class c, String n) throws InstantiationException,
        IllegalAccessException, InvocationTargetException {
        for (Method m : c.getMethods())
            if (m.getName().equals(n))
                return m.invoke(m.getDeclaringClass().newInstance());
        return null;
    }

    public static void main(String[] args) throws IOException, InstantiationException,
        IllegalAccessException, InvocationTargetException {
        MyLoader loader = new MyLoader();
        File dir = new File(args[0]);
        for (String fileName : dir.list()) {
            if (fileName.endsWith(".class")) {
                Class c = loader.defineClass(dir.getPath() + "/" + fileName);
                System.out.println("loaded class " + c.getName());
                System.out.println(invoke(c, "getSecurityMessage"));
            }
        }
    }

}
