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
        delete hub;
        if (peerConnection != 0)
            delete peerConnection;
    }
    DownloadManager(ILogger *logger, std::ostream *out) {
        this->logger = logger;
        this->out = out;
        this->nick = std::string(generateNick());
        peerConnection = 0;
        toRead = 0;
        reading = false;
        timeout = 30;
    }

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
        if (r.getFreeSlots() == 0) {
            logger->warn("file found, but no free slots");
            return;
        }
        filenames[r.getNick()] = r.getFile();
        hub->requestPeerConnection(r.getNick());
    }

    void onPeerConnectionRequested(const std::string& ip, int port) {
        if (peerConnection != 0)
            return;
        try {
            peerConnection = new PeerConnection(logger, this, ip, port);
        } catch (Exception e) {
            logger->warn("peer error: " + e.getMessage());
        }
    }

    void onPeerConnected(PeerConnection *peer) {
        peer->handshake(nick);
    }
    void onFileLengthReceived(PeerConnection *peer, int length) {
        toRead = length;
        this->length = length;
        reading = true;
        peer->send(toRead > 40906 ? 40906 : toRead);
    }
    void onHandShakeDone(PeerConnection *peer) {
        peer->get(filenames[peer->getNick()], 1);
    }
    void onNoFreeSlots(PeerConnection *peer) {
        throw Exception("suddenly onNoFreeSlots()");
    }
    void onPeerError(PeerConnection *peer, const std::string& error) {
        throw Exception("suddenly onPeerError()");
    }
    void onPeerData(PeerConnection *peer, const std::string& data) {
        out->write(data.c_str(), data.length());
        toRead -= data.length();
        peer->send(toRead > 40906 ? 40906 : toRead);
        logger->debug("got " + StringUtils::itoa(data.length()) + " bytes, " + StringUtils::itoa(toRead) + " bytes left");
        logger->info(StringUtils::itoa(100 - (int)(100*toRead/length)) + "% done");
    };


private:

    ILogger *logger;
    std::ostream *out;
    HubConnection *hub;
    PeerConnection *peerConnection;
    std::string tth;
    std::string nick;
    std::map<std::string, std::string> filenames;
    int start;
    int toRead;
    int length;
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

