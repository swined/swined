#ifndef _FILELENGTHHANDLER_H
#define	_FILELENGTHHANDLER_H

class FileLengthHandler : public virtual IPeerHandler {
public:
    FileLengthHandler(PeerConnection *conn, IPeerEventHandler *handler) {
        this->conn = conn;
        this->handler = handler;
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
        if (data.find("$FileLength") != 0)
            return;
        handler->onFileLengthReceived(conn, atoi(StringUtils::split(data, ' ', 2)[1].c_str()));
    }

private:

    PeerConnection *conn;
    IPeerEventHandler *handler;

};

#endif	/* _FILELENGTHHANDLER_H */

