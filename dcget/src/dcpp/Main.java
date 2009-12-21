package dcpp;

import java.io.FileOutputStream;
import logger.ConsoleLogger;

public class Main {

    public static void main(String[] args) throws Exception {
        FileOutputStream file = new FileOutputStream(args[3]);
        ConsoleLogger logger = new ConsoleLogger();
        DownloadManager m = new DownloadManager(logger, file);
        m.download(args[0], new Integer(args[1]), args[2]);
        logger.debug("done");
    }

}
