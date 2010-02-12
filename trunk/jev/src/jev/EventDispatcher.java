package jev;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

public class EventDispatcher {

    private final Set handlers = new HashSet();

    private class ProxyHandler implements InvocationHandler {

        private final Class cl;
        private final Set handlers;

        public ProxyHandler(Class cl, Set handlers) {
            this.cl = cl;
            this.handlers = handlers;
        }

        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            for (Object handler : handlers) {
                if (cl.isAssignableFrom(handler.getClass()))
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
        final ProxyHandler handler = new ProxyHandler(c, handlers);
        return (T)Proxy.newProxyInstance(loader, interfaces, handler);
    }

}
