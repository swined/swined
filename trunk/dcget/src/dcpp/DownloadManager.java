package dcpp;

import hub.IHubEventHandler;
import hub.SearchResult;
import logger.ILogger;

public class DownloadManager implements IHubEventHandler {

    private ILogger logger;

    public void onHubConnected() {
        logger.warn("hub connected");
    }

    public void onSearchResult(SearchResult r) {
        logger.warn(r.getNick());
    }

    public void onPeerConnectionRequested(String ip) {
        logger.warn(ip);
    }

}
