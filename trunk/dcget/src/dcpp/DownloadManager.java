package dcpp;

import hub.HubConnection;
import hub.IHubEventHandler;
import hub.SearchResult;
import java.io.OutputStream;
import java.util.Date;
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
    private OutputStream out;
    private Integer toRead;
    private int timeout = 60000;
    private int length = 0;

    public DownloadManager(ILogger logger, OutputStream out) {
        this.logger = logger;
        this.nick = generateNick();
        this.out = out;
        this.toRead = null;
    }

    public void download(String host, int port, String tth) throws Exception {
        hub = new HubConnection(this, logger, host, port, nick);
        this.tth = tth;
        Date start = new Date();
        while (toRead == null || toRead != 0) {
            hub.run();
            if (peerConnection != null)
                peerConnection.run();
            if (new Date().getTime() - start.getTime() > timeout && peerConnection == null)
                throw new Exception("search timed out");
            if (toRead != null && toRead < 0)
                throw new Exception("shit happened: need to download " + toRead + " bytes, which is a negative value");
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
        if (r.getFreeSlots() < 1) {
            logger.warn("file found, but no free slots");
            return;
        }
        length = r.getLength();
        toRead = r.getLength();
        hub.requestPeerConnection(r.getNick());
    }

    public void onPeerConnectionRequested(String ip, int port) throws Exception {
        if (peerConnection != null)
            return;
        try {
            peerConnection = new PeerConnection(logger, this, ip, port);
            logger.info("connected to " + ip + ":" + port);
        } catch (Exception e) {
            logger.warn("peer error: " + e.getMessage());
        }
    }

    public void onPeerConnected(PeerConnection peer) throws Exception {
        peer.handshake(nick);
    }

    public void onHandShakeDone(PeerConnection peer) throws Exception {
        adcGet(peer);
    }

    public void onNoFreeSlots(PeerConnection peer) throws Exception {
        throw new Exception("no free slots");
    }

    public void onPeerError(PeerConnection peer, String err) throws Exception {
        throw new Exception(err);
    }

    private void adcGet(PeerConnection peer) throws Exception {
        peer.adcGet(tth, length - toRead, toRead > 40906 ? 40906 : toRead);
    }

    public void onPeerData(PeerConnection peer, byte[] data) throws Exception {
        out.write(data);
        toRead -= data.length;
        logger.debug("got " + data.length + " bytes, " + toRead + " of " + length + " bytes left");
        logger.info((int)(100*(1 - (float)toRead/(float)length)) + "% done");
        if (toRead == 0)
            return;
        if (toRead < 0)
            throw new Exception("negative toRead detected");
        adcGet(peer);
    }

    public void onSupportsReceived(PeerConnection peer, String[] features) throws Exception {
        boolean adcGet = false;
        for (String feature : features)
            if (feature.equalsIgnoreCase("ADCGet"))
                adcGet = true;
        if (!adcGet)
            throw new Exception("peer does not support adcget");
        String supports = "";
        for (String feature : features)
            supports += feature + " ";
        logger.info("peer supports features: " + supports);
    }

}
