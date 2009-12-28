#ifndef _PEERCONNECTION_H
#define	_PEERCONNECTION_H

#include <string>
#include "ILogger.h"
#include "IPeerEventHandler.h"
#include "PeerReader.h"
#include "PeerWriter.h"
#include "PeerLogger.h"
#include "MyNickHandler.h"
#include "FileLengthHandler.h"
#include "LockHandler.h"
#include "KeyHandler.h"
#include "PeerLockHandler.h"
#include "ErrorHandler.h"
#include "MaxedOutHandler.h"
#include "DataHandler.h"

class PeerConnection {
public:
    
    PeerConnection(const PeerConnection& orig) {
        throw Exception("suddenly PeerConnection(&)");
    }

    virtual ~PeerConnection() {
        throw Exception("suddenly ~PeerConnection()");
    }

    PeerConnection(ILogger *logger, IPeerEventHandler *handler, const std::string& ip, int port) {
        this->logger = new PeerLogger(logger, ip);
        this->handler = handler;
        connect(ip, port);
    }

    void run() {
        reader->read();
    }

    void connect(const std::string& ip, int port) {
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

    void handshake(const std::string& nick) {
        writer->sendMyNick(nick);
        writer->sendLock("some_random_lock", "kio_dcpp");
    }

    void get(const std::string& file, int start) {
        writer->sendGet(file, start);
    }

    void onKeyReceived() {
        handler->onHandShakeDone(this);
    }

    void onLockReceived(const std::string& lock) {
        writer->sendDirection("Download", 42000);
        writer->sendKey(KeyGenerator::generateKey(lock));
    }

    void onPeerNickReceived(const std::string& nick) {
        this->nick = nick;
    }

    std::string getNick() {
        return nick;
    }

    void send(int len) {
        writer->sendSend();
        reader->expect(len);
    }

private:

    ILogger *logger;
    IPeerEventHandler *handler;
    PeerReader *reader;
    PeerWriter *writer;
    std::string nick;
    Socket *sock;

};

#endif	/* _PEERCONNECTION_H */

