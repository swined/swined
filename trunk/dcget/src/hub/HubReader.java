package hub;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import logger.ILogger;
import util.ArrayUtils;


class HubReader {

    private ILogger logger;
    private InputStream in;
    private byte[] buffer = new byte[0];
    private byte[] read = new byte[1024];
    private Set<IHubHandler> handlers = new HashSet();

    public HubReader(InputStream in, ILogger logger) {
        this.in = in;
        this.logger = logger;
    }

    private void readStream() throws Exception {
        if (in.available() == 0)
            return;
        int c = in.read(read);
        if (c == 0)
            return;
        if (c == -1)
            throw new Exception("read failed");
        buffer = ArrayUtils.append(buffer, read, c);
    }

    private byte[] readCommand() throws Exception {
        readStream();
        int ix = ArrayUtils.indexOf(buffer, (byte)0x7C); // |
        if (ix != -1) {
            byte[] b = ArrayUtils.sub(buffer, 0, ix - 1);
            buffer = ArrayUtils.sub(buffer, ix + 1, buffer.length - 1);
            return b;
        }
        return null;
    }

    public void registerHandler(IHubHandler handler) {
        handlers.add(handler);
    }

    public void read() throws Exception {
        byte[] data = readCommand();
        if (data == null)
            return;
        logger.debug("received hub command: " + new String(data));
        for (IHubHandler handler : handlers)
            handler.handleHubCommand(data);
    }

}
