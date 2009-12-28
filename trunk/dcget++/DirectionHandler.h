#ifndef _DIRECTIONHANDLER_H
#define	_DIRECTIONHANDLER_H

class DirectionHandler : public virtual IPeerHandler {
public:
    DirectionHandler(PeerConnection *conn, IPeerEventHandler *handler) {
        throw Exception("suddenly DirectionHandler()");
    }
    DirectionHandler(const DirectionHandler& orig) {
        throw Exception("suddenly DirectionHandler(&)");
    }
    virtual ~DirectionHandler() {
        throw Exception("suddenly ~DirectionHandler()");
    }

    void handlePeerData(const std::string& data) {
        throw Exception("suddenly DirectionHandler::handlePeerData()");
    }

    void handlePeerCommand(const std::string& data) {
        throw Exception("suddenly DirectionHandler::handlePeerCommand()");
    }

private:

    PeerConnection *conn;
    IPeerEventHandler *handler;

};

#endif	/* _DIRECTIONHANDLER_H */

