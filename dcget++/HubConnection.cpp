#include "HubConnection.h"

#include "LockHandler.h"
#include "SRHandler.h"
#include "ConnectToMeHandler.h"

HubConnection::HubConnection(IHubEventHandler *handler, ILogger *logger, const std::string& host, int port, const std::string& nick)
{
    this->nick = nick;
    this->handler = handler;
    logger->debug("connecting to " + host);
    this->sock = new Socket();
    sock->connect(host, port);
    sock->set_non_blocking(true);
    logger->debug("connected");
    this->writer = std::auto_ptr<HubWriter>(new HubWriter(sock, logger));
    this->reader = std::auto_ptr<HubReader>(new HubReader(sock, logger));
    reader->registerHandler(new LockHandler(this));
    reader->registerHandler(new SRHandler(handler));
    reader->registerHandler(new ConnectToMeHandler(handler));
}

