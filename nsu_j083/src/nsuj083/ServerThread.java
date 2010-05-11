package nsuj083;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

public class ServerThread<T> implements Runnable {

    private final T object;
    private final T proxy;
    private final List<Task> tasks = new LinkedList<Task>();

    public ServerThread(Class<T> cl, T object) {
        this.object = object;
        ClassLoader loader = cl.getClassLoader();
        Class<?>[] interfaces = new Class[]{cl};
        ProxyHandler handler = new ProxyHandler();
        this.proxy = (T) Proxy.newProxyInstance(loader, interfaces, handler);
    }

    private class ProxyHandler implements InvocationHandler {

        public Object invoke(Object proxy, Method m, Object[] args)
                throws Throwable {
            Task task = new Task(object, m, args);
            run(task);
            return task.getResult();
        }
    }

    private synchronized void run(Task task) {
        tasks.add(task);
        notify();
    }

    private synchronized Task getTask() throws InterruptedException {
        while (tasks.isEmpty()) {
            wait();
        }
        return tasks.remove(0);
    }

    public void run() {
        while (true) {
            try {
                getTask().run();
            } catch (Throwable e) {
                System.err.println("[srv/" + Thread.currentThread().getName() + "] " + e);
            }
        }
    }

    public T instance() {
        return proxy;
    }
}
