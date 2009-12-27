#ifndef _HUBCONNECTION_H
#define	_HUBCONNECTION_H

#include "IHubEventHandler.h"
#include "ILogger.h"
#include "HubReader.h"
#include "HubWriter.h"

class HubConnection {
public:
    HubConnection(const HubConnection& orig) {
        throw Exception("suddenly copy constructor");
    }
    virtual ~HubConnection() {}

    HubConnection(IHubEventHandler *handler, ILogger *logger, const std::string& host, int port, const std::string& nick);
    void run() {
        reader->run();
    }
    void onHubConnected(const std::string& lock) {
        //throw Exception((std::string("suddenly onHubConnected(): ") + lock).c_str());
        writer->sendKey(KeyGenerator.generateKey(lock));
        writer->sendValidateNick(nick);
        writer->sendVersion("0.01");
        writer->sendMyInfo(nick);
        handler->onHubConnected();
    }
    void search(const std::string& tth) {
        throw Exception("suddenly search()");
    }
    void requestPeerConnection(const std::string& target) {
        throw Exception("suddenly peer connection requested");
    }

private:

    IHubEventHandler *handler;
    HubReader *reader;
    HubWriter *writer;
    std::string nick;


};

#endif	/* _HUBCONNECTION_H */

