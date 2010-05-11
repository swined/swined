package nsuj083;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Task {

    private final Object object;
    private final Method method;
    private final Object[] args;
    private boolean done = false;
    private Object result = null;
    private Throwable exception = null;

    public Task(Object object, Method method, Object[] args) {
        this.object = object;
        this.method = method;
        this.args = args;
    }

    public synchronized void run() {
        try {
            result = method.invoke(object, args);
        } catch (InvocationTargetException e) {
            exception = e.getCause();
        } catch (Throwable e) {
            exception = e;
        } finally {
            done = true;
            notify();
        }
    }

    public synchronized Object getResult() throws Throwable {
        while (!done) {
            wait();
        }
        if (exception == null) {
            return result;
        } else {
            throw exception;
        }
    }
}
