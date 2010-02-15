package jev;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;

public class EventDispatcher {

    private final static HashMap<Class<?>, Object> defaultValues;
    private final HashSet<Object> handlers = new HashSet<Object>();
    private final HashMap<Class<?>, Object> proxies = new HashMap<Class<?>, Object>();

    static {
        defaultValues = new HashMap<Class<?>, Object>();
        defaultValues.put(int.class, (int)0);
        defaultValues.put(byte.class, (byte)0);
        defaultValues.put(short.class, (short)0);
        defaultValues.put(long.class, (long)0);
        defaultValues.put(float.class, (float)0);
        defaultValues.put(double.class, (double)0);
        defaultValues.put(char.class, (char)0);
        defaultValues.put(boolean.class, false);
    }

    private class ProxyHandler implements InvocationHandler {

        
        private final HashSet<Object> handlers;

        public ProxyHandler(HashSet<Object> handlers) {
            this.handlers = handlers;
        }

        private Object invoke(Method m, Object[] args) throws InvocationTargetException, IllegalAccessException {
            Object result = defaultValues.get(m.getReturnType());
            final Class<?> mc = m.getDeclaringClass();
            for (Object handler : handlers)
                if (mc.isAssignableFrom(handler.getClass()))
                    result = m.invoke(handler, args);
            return result;
        }

        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            try {
                return invoke(m, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

    }

    public void register(Object handler) {
        handlers.add(handler);
    }

    public void unregister(Object handler) {
        handlers.remove(handler);
    }

    private Object createProxy(Class<?> cl) {
        final ClassLoader loader = cl.getClassLoader();
        final Class<?>[] interfaces = new Class[]{ cl };
        final ProxyHandler handler = new ProxyHandler(handlers);
        return Proxy.newProxyInstance(loader, interfaces, handler);
    }

    private Object getProxy(Class<?> cl) {
        Object proxy = proxies.get(cl);
        if (proxy == null) {
                proxy = createProxy(cl);
                proxies.put(cl, proxy);
        }
                return proxy;
    }

    @SuppressWarnings("unchecked")
        public<T> T invoke(Class<T> cl) {
        return (T)getProxy(cl);
    }

}