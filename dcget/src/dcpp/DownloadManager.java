package dcpp;

import hub.HubConnection;
import hub.IHubEventHandler;
import hub.SearchResult;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Random;
import logger.ILogger;
import peer.IPeerEventHandler;
import peer.PeerConnection;

public class DownloadManager implements IHubEventHandler, IPeerEventHandler {

    private ILogger logger;
    private HubConnection hub;
    private String tth;
    private String nick;
    private PeerConnection peerConnection;
    private HashMap<String, byte[]> filenames;
    private OutputStream out;
    private int toRead = -1;

    public DownloadManager(ILogger logger, OutputStream out) {
        this.logger = logger;
        this.nick = generateNick();
        this.out = out;
    }

    public void download(String host, int port, String tth) throws Exception {
        hub = new HubConnection(this, logger, host, port, nick);
        filenames = new HashMap();
        this.tth = tth;
        while (true) {
            hub.run();
            if (peerConnection != null)
                peerConnection.run();
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
        if (r.getFreeSlots() == 0) {
            logger.warn("file found, but no free slots");
            return;
        }
        filenames.put(r.getNick(), r.getFile());
        hub.requestPeerConnection(r.getNick());
    }

    public void onPeerConnectionRequested(String ip, int port) throws Exception {
        try {
            peerConnection = new PeerConnection(logger, this, ip, port);
        } catch (Exception e) {
            logger.warn("peer error: " + e.getMessage());
        }
    }

    public void onPeerConnected(PeerConnection peer) throws Exception {
        peer.handshake(nick);
    }

    public void onFileLengthReceived(PeerConnection peer, int length) throws Exception {
        toRead = length;
        peer.send(toRead > 40906 ? 40906 : toRead);
    }

    public void onHandShakeDone(PeerConnection peer) throws Exception {
        peer.get(filenames.get(peer.getNick()), 1);
    }

    public void onNoFreeSlots(PeerConnection peer) throws Exception {
        throw new Exception("no free slots");
    }

    public void onPeerError(PeerConnection peer, String err) throws Exception {
        throw new Exception(err);
    }

    public void onPeerData(PeerConnection peer, byte[] data) throws Exception {
        out.write(data);
        toRead -= data.length;
        peer.send(toRead > 40906 ? 40906 : toRead);
    }

}
