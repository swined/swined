package logger;

import java.util.logging.Logger;

class ConsoleLogger implements ILogger {

    public void warn(String msg) {
        System.err.println("warn: " + msg);
    }
    public void error(String msg) {
        System.err.println("error: " + msg);
    }
    public void debug(String msg) {
        System.out.println("debug: " + msg);
    }
    public void info(String msg) {
        System.out.println("info: " + msg);
    }

}
