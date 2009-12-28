#ifndef _DOWNLOADMANAGER_H
#define	_DOWNLOADMANAGER_H

#include "IHubEventHandler.h"
#include "ILogger.h"
#include "HubConnection.h"
#include "IPeerEventHandler.h"
#include <map>

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
        peerConnection = 0;
        toRead = 0;
        reading = false;
        timeout = 30000;
    }

/*    void download(const std::string& host, int port, const std::string& tth) {
        hub = new HubConnection(this, logger, host, port, nick);
        this->tth = tth;
        while (true) {
            hub->run();
        }
    }*/

    void download(const std::string& host, int port, const std::string& tth) {
        hub = new HubConnection(this, logger, host, port, nick);
        this->tth = tth;
        start = time(0);
        while (!reading || toRead != 0) {
            hub->run();
            if (peerConnection != 0)
                peerConnection->run();
            if (time(0) - start > timeout && peerConnection == 0)
                throw Exception("search timed out");
            if (reading && toRead < 0)
                throw Exception("shit happened: need to download " + StringUtils::itoa(toRead) + " bytes, which is a negative value");
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
    PeerConnection *peerConnection;
    std::string tth;
    std::string nick;
    std::map<std::string, std::string> filenames;
    int start;
    int toRead;
    bool reading;
    int timeout;

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

