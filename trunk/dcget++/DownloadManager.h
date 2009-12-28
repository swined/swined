#ifndef _DOWNLOADMANAGER_H
#define	_DOWNLOADMANAGER_H

#include "IHubEventHandler.h"
#include "ILogger.h"
#include "HubConnection.h"
#include "IPeerEventHandler.h"

class DownloadManager : public virtual IHubEventHandler, public virtual IPeerEventHandler {
public:
    DownloadManager(const DownloadManager& orig) {
        throw Exception("copy constructor suddenly");
    }
    virtual ~DownloadManager() {
        throw Exception("suddenly ~DownloadManager()");
    }
    DownloadManager(ILogger *logger) {
        this->logger = logger;
        this->nick = std::string(generateNick());
    }

    void download(const std::string& host, int port, const std::string& tth) {
        hub = new HubConnection(this, logger, host, port, nick);
        this->tth = tth;
        while (true) {
            hub->run();
        }
    }
    void onHubConnected() {
        hub->search(tth);
    }

    void onSearchResult(const SearchResult& r) {
        throw Exception("suddenly SR");
    }

    void onPeerConnectionRequested(const std::string& ip, int port) {
        throw Exception("suddenly peer connection requested");
    }

    void onPeerConnected(PeerConnection *peer) {
        throw Exception("suddenly onPeerConnected()");
    }
    void onFileLengthReceived(PeerConnection *peer, int length) {
        throw Exception("suddenly onFileLengthReceived()");
    }
    void onHandShakeDone(PeerConnection *peer) {
        throw Exception("suddenly onHandShakeDone()");
    }
    void onNoFreeSlots(PeerConnection *peer) {
        throw Exception("suddenly onNoFreeSlots()");
    }
    void onPeerError(PeerConnection *peer, const std::string& error) {
        throw Exception("suddenly onPeerError()");
    }
    void onPeerData(PeerConnection *peer, const std::string& data) {
        throw Exception("suddenly onPeerData()");
    };


private:

    ILogger *logger;
    HubConnection *hub;
    std::string tth;
    std::string nick;

    static std::string generateNick() {
        srand(time(NULL));
        std::string r = std::string();
        const std::string d("qwertyuiopasdfghjklzxcvbnm");
        for (int i = 0; i < 8; i++)
            r.append(d.substr(rand() % d.length(), 1));
        return r;
    }

};

#endif	/* _DOWNLOADMANAGER_H */

