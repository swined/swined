package dcpp;

import java.util.HashSet;
import java.util.Set;

public class DownloadManager {

    private HubReader reader;
    private HubWriter writer;
    private String nick;
    private String tth;
    private Set<PeerInfo> peers = new HashSet();

    public DownloadManager(HubReader reader, HubWriter writer, String nick, String tth) {
        this.reader = reader;
        this.writer = writer;
        this.nick = nick;
        this.tth = tth;
        reader.registerHandler(new LockHandler(this));
        reader.registerHandler(new SRHandler(this));
        reader.registerHandler(new ConnectToMeHandler(this));
    }

    public void run() throws Exception {
        reader.read();
    }

    public void onPeerFound(PeerInfo peer) throws Exception {
        peers.add(peer);
        writer.sendUserIP(peer.getNick());
        writer.sendRevConnectToMe(nick, peer.getNick());
    }

    public void onPeerIpDetected(String nick, String ip) {
        for (PeerInfo peer : peers) {
            if (peer.getNick().equals(nick)) {
                peer.setIp(ip);
            }
        }
    }

    public void onPeerConnectionRequested(String ip) throws Exception {
        throw new Exception("peer connection requested");
    }

    public void onHubConnected(String lock) throws Exception {
        writer.sendKey(KeyGenerator.generateKey(lock.getBytes()));
        writer.sendValidateNick(nick);
        writer.sendVersion("0.01");
        writer.sendMyInfo(nick);
        writer.sendTTHSearch(nick, tth);
    }
}
