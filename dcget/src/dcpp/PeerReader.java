package dcpp;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import logger.ILogger;

public class PeerReader {

    private ILogger logger;
    private InputStream in;
    private String id;
    private byte[] buffer = new byte[0];
    private byte[] read = new byte[1024];
    private Set<IPeerHandler> handlers = new HashSet();
    private int expectData = 0;

    public PeerReader(InputStream in, ILogger logger, String id) {
        this.in = in;
        this.logger = logger;
        this.id = id;
    }

    private static byte[] append(byte[] a, byte[] b, int l) {
        byte[] r = new byte[a.length + l];
        for (int i = 0; i < a.length; i++) {
            r[i] = a[i];
        }
        for (int i = 0; i < l; i++) {
            r[i + a.length] = b[i];
        }
        return r;
    }

    private void readStream() throws Exception {
        if (in.available() == 0) {
            return;
        }
        int c = in.read(read);
        if (c == 0) {
            return;
        }
        if (c == -1) {
            throw new Exception("read failed");
        }
        buffer = append(buffer, read, c);
    }

    private static int indexOf(byte[] a, byte c) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == c) {
                return i;
            }
        }
        return -1;
    }

    private static byte[] sub(byte[] a, int s, int e) {
        byte[] b = new byte[e - s + 1];
        for (int i = s; i <= e; i++) {
            b[i - s] = a[i];
        }
        return b;
    }

    private byte[] readCommand() throws Exception {
        readStream();
        int ix = indexOf(buffer, (byte) 0x7C); // |
        if (ix != -1) {
            byte[] b = sub(buffer, 0, ix - 1);
            buffer = sub(buffer, ix + 1, buffer.length - 1);
            return b;
        }
        return null;
    }

    private byte[] readData() throws Exception {
        readStream();
        if (buffer.length < expectData) {
            return null;
        }
        byte[] b = sub(buffer, 0, expectData - 1);
        buffer = sub(buffer, expectData, buffer.length - 1);
        expectData = 0;
        return b;
    }

    public void registerHandler(IPeerHandler handler) {
        handlers.add(handler);
    }

    public void expectData(int e) {
        expectData = e;
    }

    public void read() throws Exception {
        if (expectData > 0) {
            byte[] data = readData();
            if (data == null) {
                return;
            }
            logger.debug("received data from " + id + ": " + data.length + " bytes");
            for (IPeerHandler handler : handlers) {
                handler.handlePeerData(data);
            }
        } else {
            byte[] data = readCommand();
            if (data == null) {
                return;
            }
            logger.debug("received command from " + id + ": " + new String(data));
            for (IPeerHandler handler : handlers) {
                handler.handlePeerCommand(data);
            }

        }
    }
}
