package dcpp;

import java.io.IOException;
import java.io.OutputStream;
import logger.ILogger;

public class HubWriter {

    private ILogger logger;
    private OutputStream out;

    public HubWriter(OutputStream out, ILogger logger) {
        this.out = out;
        this.logger = logger;
    }

    private void sendString(String s) throws IOException {
        out.write(s.getBytes());
        logger.debug("sent string to hub: " + s);
    }

    public void sendValidateNick(String nick) throws IOException {
        sendString("$ValidateNick " + nick + "|");
    }

    public void sendKey(String key) throws IOException {
        sendString("$Key " + key + "|");
    }

    public void sendVersion(String version) throws IOException {
        sendString("$Version " + version + "|");
    }

    public void sendMyInfo(String nick) throws IOException {
        sendString("$MyINFO $ALL " + nick + " $ $56k" + new String(new byte[] { 0x08 }) + "$$0$|");
    }
    
    public void sendTTHSearch(String nick, String tth) throws IOException {
        sendString("$Search Hub:" + nick + " F?F?0?9?TTH:" + tth + "|");
    }

    public void sendRevConnectToMe(String nick, String target) throws IOException {
        sendString("$RevConnectToMe " + nick + " " + target + "|");
    }

    public void sendUserIP(String target) throws IOException {
        sendString("$UserIP " + target + "|");
    }

}
