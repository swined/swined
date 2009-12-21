package dcpp;

import hub.HubConnection;
import hub.IHubEventHandler;
import hub.SearchResult;
import java.util.HashMap;
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
    private String nick;
    private Set<PeerConnection> peers;
    private HashMap<String, String> filenames;

    public DownloadManager(ILogger logger) throws Exception {
        this.logger = logger;
        this.nick = generateNick();
    }

    public void download(String host, int port, String tth) throws Exception {
        hub = new HubConnection(this, logger, host, port, nick);
        peers = new HashSet();
        filenames = new HashMap();
        this.tth = tth;
        while (true) {
            hub.run();
            updatePeers();
        }
    }

    private String generateNick() {
        Random rand = new Random();
        byte[] bytes = new byte[8];
        rand.nextBytes(bytes);
        String r = "";
        for (byte b : bytes) {
            r += Integer.toHexString(b > 0 ? b : b + 0xFF);
        }
        return r;
    }

    public void onHubConnected() throws Exception {
        hub.search(tth);
    }

    public void onSearchResult(SearchResult r) throws Exception {
        filenames.put(r.getNick(), r.getFile());
        hub.requestPeerConnection(r.getNick());
    }

    public void onPeerConnectionRequested(String ip, int port) throws Exception {
        try {
            peers.add(new PeerConnection(logger, this, ip, port));
        } catch (Exception e) {
            logger.warn("peer error: " + e.getMessage());
        }
    }

    public void onPeerConnected(PeerConnection peer) throws Exception {
        peer.handshake(nick);
    }

    public void onFileLengthReceived(PeerConnection peer, int length) throws Exception {
        throw new Exception("file length received");
    }

    public void onHandShakeDone(PeerConnection peer) throws Exception {
        peer.get(filenames.get(peer.getNick()), 1);
    }

    private void updatePeers() {
        Set<PeerConnection> dead = new HashSet<PeerConnection>();
        for (PeerConnection peer : peers) {
            try {
                peer.run();
            } catch (Exception e) {
                logger.warn("peer error: " + e.getMessage());
                dead.add(peer);
            }
        }
        for (PeerConnection peer : dead) {
            peers.remove(peer);
        }
    }

}
