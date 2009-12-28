#ifndef _FILELENGTHHANDLER_H
#define	_FILELENGTHHANDLER_H

class FileLengthHandler : public virtual IPeerHandler {
public:
    FileLengthHandler(PeerConnection *conn, IPeerEventHandler *handler) {
        throw Exception("suddenly FileLengthHandler()");
    }
    FileLengthHandler(const FileLengthHandler& orig) {
        throw Exception("suddenly FileLengthHandler(&)");
    }
    virtual ~FileLengthHandler() {
        throw Exception("suddenly ~FileLengthHandler()");
    }

    void handlePeerData(const std::string& data) {
        throw Exception("suddenly FileLengthHandler::handlePeerData()");
    }

    void handlePeerCommand(const std::string& data) {
        throw Exception("suddenly FileLengthHandler::handlePeerCommand()");
    }

private:

    PeerConnection *conn;
    IPeerEventHandler *handler;

};

#endif	/* _FILELENGTHHANDLER_H */

