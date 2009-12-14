package dcpp;


public class PeerInfo {

    private String nick;
    private String file;
    private String ip;

    public PeerInfo(String nick, String file) {
        this.nick = nick;
        this.file = file;
    }

    public boolean isIpKnown() {
        return ip != null;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNick() {
        return nick;
    }

}
