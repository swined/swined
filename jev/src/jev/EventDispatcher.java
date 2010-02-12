package jev;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

public class EventDispatcher {

    private final Set handlers = new HashSet();

    private class ProxyHandler implements InvocationHandler {

        private final Set handlers;

        public ProxyHandler(Set handlers) {
            this.handlers = handlers;
        }

        public Object invoke(Object proxy, Method m, Object[] args)
                throws IllegalAccessException, InvocationTargetException {
            final Class mc = m.getDeclaringClass();
            for (Object handler : handlers) {
                final Class hc = handler.getClass();
                if (mc.isAssignableFrom(hc))
                    m.invoke(handler, args);
            }
            return null;
        }

    }

    public void register(Object handler) {
        handlers.add(handler);
    }

    public<T> T invoke(Class<T> c) {
        final ClassLoader loader = c.getClassLoader();
        final Class[] interfaces = new Class[]{ c };
        final ProxyHandler handler = new ProxyHandler(handlers);
        return (T)Proxy.newProxyInstance(loader, interfaces, handler);
    }

}
