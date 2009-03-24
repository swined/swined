package nntpd;

import java.io.IOException;

public class NntpClient {

    INntpDataProvider provider;
    NntpStreamReader reader;
    NntpStreamWriter writer;

    public NntpClient(INntpDataProvider provider, NntpStreamReader reader, NntpStreamWriter writer) {
        this.provider = provider;
        this.reader = reader;
        this.writer = writer;
    }

    public void run() throws IOException {
        String line;
        writer.writeNntpResponse("201 fake nntp server ready - no posting allowed");
        while (true) {
            line = reader.readNntpCommand();
            if (null == line)
                break;
            String[] c = line.split(" ", 2);
            try {
                if ("MODE".equals(c[0])) {
                    this.MODE(c[1]);
                } else if ("GROUP".equals(c[0])) {
                    this.GROUP(c[1]);
                } else throw new NntpException("500 unknown command");
            } catch (NntpException e) {
                writer.writeNntpResponse(e.response);
            }
        }
    }

    private void MODE(String mode) throws IOException, NntpException {
        if ("READER".equals(mode)) {
            writer.writeNntpResponse("201 fake nntp server ready - no posting allowed");
            return;
        } else {
            throw new NntpException("501 unknown mode");
        }
    }

    private void GROUP(String group) throws IOException, NntpException {
        StringBuilder sb = new StringBuilder();
        sb.append("211 ");
        sb.append(new Integer(provider.estimateCount(group)));
        sb.append(" ");
        sb.append(new Integer(provider.getFirstId(group)));
        sb.append(" ");
        sb.append(new Integer(provider.getLastId(group)));
        sb.append(" ");
        sb.append(group);
        sb.append(" group selected");
        writer.writeNntpResponse(sb.toString());
    }

}
