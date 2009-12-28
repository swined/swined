#ifndef _PEERLOCKHANDLER_H
#define	_PEERLOCKHANDLER_H

class PeerLockHandler : public virtual IPeerHandler {
public:
    PeerLockHandler(PeerConnection *conn) {
        this->conn = conn;
    }
    PeerLockHandler(const PeerLockHandler& orig) {
        throw Exception("suddenly PeerLockHandler(&)");
    }
    virtual ~PeerLockHandler() {
        throw Exception("suddenly ~PeerLockHandler()");
    }

    void handlePeerData(const std::string& data) {
        throw Exception("suddenly PeerLockHandler::handlePeerData()");
    }

    void handlePeerCommand(const std::string& data) {
        if (data.find("$Lock") != 0)
            return;
        conn->onLockReceived(StringUtils::split(data, ' ', 2)[1]);
    }

private:

    PeerConnection *conn;

};

#endif	/* _PEERLOCKHANDLER_H */

