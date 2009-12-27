#include "HubConnection.h"

#include "LockHandler.h"
#include "SRHandler.h"
#include "ConnectToMeHandler.h"

HubConnection::HubConnection(IHubEventHandler *handler, ILogger *logger, const std::string& host, int port, const std::string& nick)
: reader(sock, logger), writer(sock, logger), nick(nick) {
    this->handler = handler;
    logger->debug("connecting to " + host);
    sock.create();
    sock.connect(host, port);
    sock.set_non_blocking(true);
    logger->debug("connected");
    reader.registerHandler(new LockHandler(this));
    reader.registerHandler(new SRHandler(handler));
    reader.registerHandler(new ConnectToMeHandler(handler));
}

