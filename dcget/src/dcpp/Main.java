package dcpp;

import java.io.FileOutputStream;
import logger.ConsoleLogger;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0)
        args = new String[] {
            "p2p.academ.org",
            "411",
            "STMUNEWY73LI5KQCVMLWXDMGXZKD76GPJ3M6EQA",
            "/tmp/dcget.out",
        };
        ConsoleLogger logger = new ConsoleLogger(true);
        try {
            FileOutputStream file = new FileOutputStream(args[3]);
            DownloadManager m = new DownloadManager(logger, file);
            m.download(args[0], new Integer(args[1]), args[2]);
            logger.debug("done");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
