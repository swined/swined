package nntpd;

public class NntpException extends Exception {

    String response;

    public NntpException(String response) {
        this.response = response;
    }

}
