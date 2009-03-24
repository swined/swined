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
                } else if ("STAT".equals(c[0])) {
                    this.STAT(c[1]);
                } else if ("NEXT".equals(c[0])) { 
                    this.NEXT(null);
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
        this.group = group;
    }

    private void STAT(String id) throws IOException {
        Integer iid = new Integer(id);
        article = iid;
        writer.writeNntpResponse("223 " + id + " " + provider.getMsgId(group, iid) + " article retrieved - statistics only");
    }

    private void NEXT(String id) throws IOException {
        if (null == group) {
            writer.writeNntpResponse("412 no newsgroup selected");
            return;
        }
        if (null == article) {
            writer.writeNntpResponse("420 no current article has been selected");
            return;
        }
        if (provider.getLastId(group) <= article) {
            writer.writeNntpResponse("421 no next article in this group");
            return;
        }
        article++;
        writer.writeNntpResponse("223 " + id + " " + provider.getMsgId(group, article) + " article retrieved - statistics only");
    }

}
