package dcpp;

import java.io.FileOutputStream;
import logger.ConsoleLogger;

public class Main {

    public static void main(String[] args) throws Exception {
        FileOutputStream file = new FileOutputStream("/home/sw/dcget.out");
        ConsoleLogger l = new ConsoleLogger();
        DownloadManager m = new DownloadManager(l, file);
        m.download("p2p.academ.org", 411, "WXCQCYNWND57VJIFKIN45H6WYRHGDRS5NMLIDVY");
    }

}
