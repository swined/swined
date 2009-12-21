package dcpp;

import hub.HubConnection;
import hub.IHubEventHandler;
import hub.SearchResult;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import logger.ILogger;
import peer.IPeerEventHandler;
import peer.PeerConnection;

public class DownloadManager implements IHubEventHandler, IPeerEventHandler {

    private ILogger logger;
    private HubConnection hub;
    private String tth;
    private Set<PeerConnection> peers;

    public DownloadManager(ILogger logger) throws Exception {
        this.logger = logger;
    }

    public void download(String host, int port, String tth) throws Exception {
        hub = new HubConnection(this, logger, host, port, generateNick());
        peers = new HashSet();
        this.tth = tth;
        while (true) {
            hub.run();
            for (PeerConnection peer : peers)
                peer.run();
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
        peers.add(new PeerConnection(logger, this, ip, port));
    }

    public void onPeerConnected(PeerConnection peer) throws Exception {
        throw new Exception("peer connected");
    }

}
