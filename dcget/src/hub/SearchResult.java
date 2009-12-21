package hub;

public class SearchResult {

    private String nick;
    private byte[] file;
    private int freeSlots;
    private int totalSlots;

    public SearchResult(String nick, byte[] file, int freeSlots, int totalSlots) {
        this.nick = nick;
        this.file = file;
        this.freeSlots = freeSlots;
        this.totalSlots = totalSlots;
    }

    public String getNick() {
        return nick;
    }

    public byte[] getFile() {
        return file;
    }

    public int getFreeSlots() {
        return freeSlots;
    }

    public int getTotalSlots() {
        return freeSlots;
    }

}
