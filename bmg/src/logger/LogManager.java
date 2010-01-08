package logger;

public class LogManager {

    public static ILogger getLogger() {
        return new ConsoleLogger();
    }

}
