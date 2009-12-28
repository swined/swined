#ifndef _MAXEDOUTHANDLER_H
#define	_MAXEDOUTHANDLER_H

class MaxedOutHandler : public virtual IPeerHandler {
public:
    MaxedOutHandler(PeerConnection *conn, IPeerEventHandler *handler) {
        throw Exception("suddenly MaxedOutHandler()");
    }
    MaxedOutHandler(const MaxedOutHandler& orig) {
        throw Exception("suddenly MaxedOutHandler(&)");
    }
    virtual ~MaxedOutHandler() {
        throw Exception("suddenly ~MaxedOutHandler()");
    }

    void handlePeerData(const std::string& data) {
        throw Exception("suddenly MaxedOutHandler::handlePeerData()");
    }

    void handlePeerCommand(const std::string& data) {
        throw Exception("suddenly MaxedOutHandler::handlePeerCommand()");
    }

private:

    PeerConnection *conn;
    IPeerEventHandler *handler;

};

#endif	/* _MAXEDOUTHANDLER_H */

