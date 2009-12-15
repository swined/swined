package dcpp;

import hub.HubConnection;
import hub.IHubEventHandler;
import hub.SearchResult;
import java.util.Random;
import logger.ILogger;

public class DownloadManager implements IHubEventHandler {

    private ILogger logger;
    private HubConnection hub;
    private String tth;

    public DownloadManager(ILogger logger) throws Exception {
        this.logger = logger;
    }

    public void download(String host, int port, String tth) throws Exception {
        hub = new HubConnection(this, logger, host, port, generateNick());
        this.tth = tth;
        while (true) {
            hub.run();
        }
    }

    private String generateNick() {
        Random rand = new Random();
        byte[] bytes = new byte[8];
        rand.nextBytes(bytes);
        String r = "";
        for (byte b : bytes)
            r += Integer.toHexString(b > 0 ? b : b + 0xFF);
        return r;
    }

    public void onHubConnected() throws Exception {
        hub.search(tth);
    }

    public void onSearchResult(SearchResult r) throws Exception {
        hub.requestPeerConnection(r.getNick());
    }

    public void onPeerConnectionRequested(String ip, int port) throws Exception {
        throw new Exception("peer connection (" + ip + ":" + port + ") requested");
    }

}
