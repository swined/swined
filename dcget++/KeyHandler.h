#ifndef _KEYHANDLER_H
#define	_KEYHANDLER_H

class KeyHandler : public virtual IPeerHandler {
public:
    KeyHandler(PeerConnection *conn, IPeerEventHandler *handler) {
        this->conn = conn;
        this->handler = handler;
    }
    KeyHandler(const KeyHandler& orig) {
        throw Exception("suddenly KeyHandler(&)");
    }
    virtual ~KeyHandler() {
        throw Exception("suddenly ~KeyHandler()");
    }

    void handlePeerData(const std::string& data) {
        throw Exception("suddenly KeyHandler::handlePeerData()");
    }

    void handlePeerCommand(const std::string& data) {
        if (data.find("$Key") != 0)
            return;
        conn->onKeyReceived();
    }

private:

    PeerConnection *conn;
    IPeerEventHandler *handler;

};

#endif	/* _KEYHANDLER_H */

