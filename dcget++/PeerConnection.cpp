#include "PeerConnection.h"

#include "MyNickHandler.h"
#include "FileLengthHandler.h"
#include "LockHandler.h"
#include "KeyHandler.h"
#include "PeerLockHandler.h"
#include "ErrorHandler.h"
#include "MaxedOutHandler.h"
#include "DataHandler.h"

void PeerConnection::connect(const std::string& ip, int port) {
    logger->debug("connecting to " + ip);
    sock = new Socket();
    sock->connect(ip, port);
    sock->set_non_blocking(true);
    logger->debug("connected");
    reader = new PeerReader(sock, logger);
    reader->registerHandler(new MyNickHandler(handler, this));
    reader->registerHandler(new FileLengthHandler(this, handler));
    reader->registerHandler(new PeerLockHandler(this));
    //reader->registerHandler(new DirectionHandler(handler, this));
    reader->registerHandler(new KeyHandler(this, handler));
    reader->registerHandler(new ErrorHandler(this, handler));
    reader->registerHandler(new MaxedOutHandler(this, handler));
    reader->registerHandler(new DataHandler(this, handler));
    writer = new PeerWriter(sock, logger);
    handler->onPeerConnected(this);
}

void PeerConnection::onKeyReceived() {
    handler->onHandShakeDone(this);
}