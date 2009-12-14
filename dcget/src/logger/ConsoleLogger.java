package logger;

public class ConsoleLogger implements ILogger{

    public void info(String m) {
        System.out.println("info: " + m);
    }

    public void debug(String m) {
        System.out.println("debug: " + m);
    }

    public void warn(String m) {
        System.err.println("warn: " + m);
    }

    public void error(String m) {
        System.err.println("error: " + m);
    }


}
