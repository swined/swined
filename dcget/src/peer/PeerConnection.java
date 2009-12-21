package peer;

import java.net.Socket;
import util.KeyGenerator;
import logger.ILogger;

public class PeerConnection {

    private ILogger logger;
    private IPeerEventHandler handler;
    private PeerReader reader;
    private PeerWriter writer;
    private String nick;

    public PeerConnection(ILogger logger, IPeerEventHandler handler, String ip, int port) throws Exception {
        this.logger = new PeerLogger(logger, ip);
        this.handler = handler;
        connect(ip, port);
    }

    public void run() throws Exception {
        reader.read();
    }

    private void connect(String ip, int port) throws Exception {
        Socket sock = new Socket(ip, port);
        reader = new PeerReader(sock.getInputStream(), logger);
        reader.registerHandler(new MyNickHandler(handler, this));
        reader.registerHandler(new FileLengthHandler(handler, this));
        reader.registerHandler(new LockHandler(this));
        reader.registerHandler(new DirectionHandler(handler, this));
        writer = new PeerWriter(sock.getOutputStream(), logger);
        handler.onPeerConnected(this);
    }

    public void handshake(String nick) throws Exception {
        writer.sendMyNick(nick);
        writer.sendLock("some_random_lock", "kio_dcpp");
    }

    public void get(String file, int start) throws Exception {
        writer.sendGet(file, start);
    }

    public void onLockReceived(String lock) throws Exception {
        writer.sendKey(KeyGenerator.generateKey(lock.getBytes()));
    }

    public void onDirectionReceived(String direction, int i) throws Exception {
        writer.sendDirection("Download", i + 1);
        handler.onHandShakeDone(this);
    }

    public void onPeerNickReceived(String nick) throws Exception {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

}
