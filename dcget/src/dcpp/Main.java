package dcpp;

import logger.ConsoleLogger;

public class Main {

    public static void main(String[] args) throws Exception {
        ConsoleLogger l = new ConsoleLogger();
        DownloadManager m = new DownloadManager(l);
        m.download("p2p.academ.org", 411, "WXCQCYNWND57VJIFKIN45H6WYRHGDRS5NMLIDVY");
    }

}
