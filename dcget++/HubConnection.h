#ifndef _HUBCONNECTION_H
#define	_HUBCONNECTION_H

#include "HubReader.h"
#include "HubWriter.h"
#include "Exception.h"
#include "Socket.h"
#include "LockHandler.h"
#include "SRHandler.h"
#include "ConnectToMeHandler.h"

class HubConnection {
public:
    HubConnection(const HubConnection& orig) {
        throw Exception("suddenly copy constructor");
    }
    virtual ~HubConnection() {}

    HubConnection(IHubEventHandler *handler, ILogger *logger, const std::string& host, int port, const std::string& nick) {
        this->handler = handler;
        logger->debug(std::string("connecting to ") + host);
        Socket *sock = new Socket();
        sock->create();
        sock->connect(host, port);
        sock->set_non_blocking(true);
        logger->debug(std::string("connected"));
        this->reader = new HubReader(sock, logger);
        this->writer = new HubWriter(sock, logger);
        this->nick = std::string(nick);
        reader->registerHandler(new LockHandler(this));
        reader->registerHandler(new SRHandler(handler));
        reader->registerHandler(new ConnectToMeHandler(handler));
    }
    void run() {
        reader->run();
    }
    void onHubConnected(const std::string& lock) {
        throw Exception("suddenly onHubConnected()");
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

