package hub;

public class SearchResult {

    private String nick;
    private String file;

    public SearchResult(String nick, String file) {
        this.nick = nick;
        this.file = file;
    }

    public String getNick() {
        return nick;
    }

    public String getFile() {
        return file;
    }

}
