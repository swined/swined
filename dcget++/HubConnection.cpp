#include "HubConnection.h"

#include "LockHandler.h"
#include "SRHandler.h"
#include "ConnectToMeHandler.h"

HubConnection::HubConnection(IHubEventHandler *handler, ILogger *logger, const std::string& host, int port, const std::string& nick) {
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

