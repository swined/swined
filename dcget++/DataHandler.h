#ifndef _DATAHANDLER_H
#define	_DATAHANDLER_H

class DataHandler : public virtual IPeerHandler {
public:
    DataHandler(PeerConnection *conn, IPeerEventHandler *handler) {
        throw Exception("suddenly DataHandler()");
    }
    DataHandler(const DataHandler& orig) {
        throw Exception("suddenly DataHandler(&)");
    }
    virtual ~DataHandler() {
        throw Exception("suddenly ~DataHandler()");
    }

    void handlePeerData(const std::string& data) {
        throw Exception("suddenly DataHandler::handlePeerData()");
    }

    void handlePeerCommand(const std::string& data) {
        throw Exception("suddenly DataHandler::handlePeerCommand()");
    }

private:

    PeerConnection *conn;
    IPeerEventHandler *handler;

};

#endif	/* _DATAHANDLER_H */

