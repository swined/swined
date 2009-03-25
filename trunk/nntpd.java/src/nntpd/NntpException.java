package nntpd;

public class NntpException extends Exception {

    int code;
    String response;

    public NntpException(int code, String response) {
        this.code = code;
        this.response = response;
    }

}
