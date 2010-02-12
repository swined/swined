package jev;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

public class EventDispatcher {

    private final Set<Object> handlers = new HashSet<Object>();

    private class ProxyHandler implements InvocationHandler {

        private final Set handlers;

        public ProxyHandler(Set handlers) {
            this.handlers = handlers;
        }

        public Object invoke(Object proxy, Method m, Object[] args)
                throws IllegalAccessException, InvocationTargetException {
            if (m.getExceptionTypes().length > 0)
                throw new IllegalArgumentException("invoking methods that can throw exceptions is not supported");
            if (m.getReturnType() != void.class)
                throw new IllegalArgumentException("invoking methods that return non-void is not supported");
            final Class<?> mc = m.getDeclaringClass();
            for (Object handler : handlers) {
                final Class hc = handler.getClass();
                if (mc.isAssignableFrom(hc))
                    m.invoke(handler, args);
            }
            return m.getDefaultValue();
        }

    }

    public void register(Object handler) {
        handlers.add(handler);
    }

    public void unregister(Object handler) {
        handlers.remove(handler);
    }

    public<T> T invoke(Class<T> c) {
        final ClassLoader loader = c.getClassLoader();
        final Class[] interfaces = new Class[]{ c };
        final ProxyHandler handler = new ProxyHandler(handlers);
        return (T)Proxy.newProxyInstance(loader, interfaces, handler);
    }

}
