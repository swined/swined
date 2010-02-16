package jev;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;

public class EventDispatcher {

    private final ProxyHandler handler = new ProxyHandler();
    private final HashMap<Class<?>, Object> proxies = new HashMap<Class<?>, Object>();

    private class ProxyHandler implements InvocationHandler {
        
        private final HashSet<Object> handlers = new HashSet<Object>();

        public void register(Object handler) {
            handlers.add(handler);
        }

        public void unregister(Object handler) {
            handlers.remove(handler);
        }

        private Object getDefaultValue(Class<?> cl) {
            if (cl == void.class)
                return null;
            Object array = Array.newInstance(cl, 1);
            return Array.get(array, 0);
        }

        private Object invoke(Method m, Object[] args) throws InvocationTargetException, IllegalAccessException {
            Object result = getDefaultValue(m.getReturnType());
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
        this.handler.register(handler);
    }

    public void unregister(Object handler) {
        this.handler.unregister(handler);
    }

    private Object createProxy(Class<?> cl) {
        ClassLoader loader = cl.getClassLoader();
        Class<?>[] interfaces = new Class[]{ cl };
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