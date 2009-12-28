#ifndef _MAXEDOUTHANDLER_H
#define	_MAXEDOUTHANDLER_H

#include "IPeerEventHandler.h"


class MaxedOutHandler : public virtual IPeerHandler {
public:
    MaxedOutHandler(PeerConnection *conn, IPeerEventHandler *handler) {
        this->conn = conn;
        this->handler = handler;
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
        if (data.find("$MaxedOut") != 0)
            return;
        handler->onNoFreeSlots(conn);
    }

private:

    PeerConnection *conn;
    IPeerEventHandler *handler;

};

#endif	/* _MAXEDOUTHANDLER_H */

