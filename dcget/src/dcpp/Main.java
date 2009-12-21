package dcpp;

import java.io.FileOutputStream;
import logger.ConsoleLogger;

public class Main {

    public static void main(String[] args) throws Exception {
        FileOutputStream file = new FileOutputStream("/home/sw/dcget.out");
        ConsoleLogger logger = new ConsoleLogger();
        DownloadManager m = new DownloadManager(logger, file);
        m.download("p2p.academ.org", 411, "W3DVLEHUKUTUVQEUG3A56M7VLNMZGSITPTFTXAI");
        logger.debug("done");
    }

}
