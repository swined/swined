package peer;

import java.io.IOException;
import java.io.OutputStream;
import logger.ILogger;

class PeerWriter {

    private ILogger logger;
    private OutputStream out;

    public PeerWriter(OutputStream out, ILogger logger) {
        this.out = out;
        this.logger = logger;
    }

    private void sendString(String s) throws IOException {
        out.write(s.getBytes());
        logger.debug("sent string to peer: " + s);
    }

    public void sendMyNick(String nick) throws Exception {
        sendString("$MyNick " + nick + "|");
    }

    public void sendLock(String lock, String pk) throws Exception {
        sendString("$Lock " + lock + " Pk=" + pk + "|");
    }

    public void sendGet(String file, int start) throws Exception {
        sendString("$Get " + file + "$" + start + "|");
    }

    public void sendKey(String key) throws IOException {
        sendString("$Key " + key + "|");
    }

    public void sendDirection(String direction, int a) throws Exception {
        sendString("$Direction " + direction + "$" + a + "|");
    }

}
