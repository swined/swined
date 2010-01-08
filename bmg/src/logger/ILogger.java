package logger;

public interface ILogger {

    void warn(String msg);
    void error(String msg);
    void debug(String msg);
    void info(String msg);

}
