package nsuj06;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Main {

    private static void print(Class c) {
        for (Method m : c.getDeclaredMethods())
            System.out.println(m);
        if (c.getSuperclass() != null)
            print(c.getSuperclass());
    }

    private static Object create(Class cl, Object... params) throws Throwable {
        Class[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++)
            paramTypes[i] = params[i].getClass();
        Constructor constructor = cl.getConstructor(paramTypes);
        return constructor.newInstance(params);
    }

    private static void invoke(Method m, String... params) throws Throwable {
        Object[] p = new Object[m.getParameterTypes().length];
        for (int i = 0; i < p.length; i++)
            p[i] = create(m.getParameterTypes()[i], params[i]);
        Object o = m.getDeclaringClass().newInstance();
        m.setAccessible(true);
        m.invoke(o, p);
    }

    private static void invoke(Class c, String n, String... p) throws Throwable {
        for (Method m : c.getDeclaredMethods())
            if (m.getName().equals(n)) {
                invoke(m, p);
                return;
            }
        if (c.getSuperclass() != null)
            invoke(c.getSuperclass(), n, p);
    }

    private void foo(Integer bar, String baz) {
        System.out.println("foo(" + bar + ", " + baz + ")");
    }

    public static void main(String[] args) throws Throwable {
        String className = args[0];
        String methodName = args.length > 1 ? args[1] : null;
        String[] methodParams = args.length > 2 
                ? new String[args.length - 2] : new String[0];
        if (methodParams.length > 0)
            for (int i = 0; i < methodParams.length; i++)
                methodParams[i] = args[i + 2];
        Class c = Class.forName(className);
        print(c);
        if (methodName != null)
            invoke(c, methodName, methodParams);
    }

}
