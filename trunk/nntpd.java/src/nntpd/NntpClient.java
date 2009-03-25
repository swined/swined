package nntpd;

import java.io.IOException;

public class NntpClient {

    INntpDataProvider provider;
    NntpStreamReader reader;
    NntpStreamWriter writer;
    String group;
    Integer article;

    public NntpClient(INntpDataProvider provider, NntpStreamReader reader, NntpStreamWriter writer) {
        this.provider = provider;
        this.reader = reader;
        this.writer = writer;
    }

    public void run() throws IOException {
        String line;
        writer.writeNntpResponse(201, "fake nntp server ready - no posting allowed");
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
                } else if ("STAT".equals(c[0])) {
                    this.STAT(c[1]);
                } else if ("NEXT".equals(c[0])) { 
                    this.NEXT();
                } else throw new NntpException(500, "unknown command");
            } catch (NntpException e) {
                writer.writeNntpResponse(e.code, e.response);
            }
        }
    }

    private void MODE(String mode) throws IOException, NntpException {
        if ("READER".equals(mode)) {
            writer.writeNntpResponse(201, "fake nntp server ready - no posting allowed");
            return;
        } else {
            throw new NntpException(501, "unknown mode");
        }
    }

    private void GROUP(String group) throws IOException, NntpException {
        StringBuilder sb = new StringBuilder();
        sb.append(new Integer(provider.estimateCount(group)));
        sb.append(" ");
        sb.append(new Integer(provider.getFirstId(group)));
        sb.append(" ");
        sb.append(new Integer(provider.getLastId(group)));
        sb.append(" ");
        sb.append(group);
        sb.append(" group selected");
        writer.writeNntpResponse(211, sb.toString());
        this.group = group;
    }

    private void STAT(String id) throws IOException, NntpException {
        if (null == group)
            throw new NntpException(412, "no newsgroup selected");
        article = new Integer(id);
        String msgId = provider.getMsgId(group, article);
        if (null == msgId)
            throw new NntpException(423, "no such article number in this group");
        writer.writeNntpResponse(223, id + " " + msgId + " article retrieved - statistics only");
    }

    private void NEXT() throws IOException, NntpException {
        if (null == group)
            throw new NntpException(412, "no newsgroup selected");
        if (null == article)
            throw new NntpException(420, "no current article has been selected");
        String msgId = provider.getMsgId(group, ++article);
        if (null == msgId)
            throw new NntpException(421, "no next article in this group");
        writer.writeNntpResponse(223, article + " " + msgId + " article retrieved - statistics only");
    }

}
