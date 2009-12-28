#ifndef _PEERCONNECTION_H
#define	_PEERCONNECTION_H

#include <string>
#include "ILogger.h"
#include "IPeerEventHandler.h"
#include "PeerReader.h"
#include "PeerWriter.h"
#include "PeerLogger.h"
#include "KeyGenerator.h"

class IPeerEventHandler;
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

    void connect(const std::string& ip, int port);

    void handshake(const std::string& nick) {
        writer->sendMyNick(nick);
        writer->sendLock("some_random_lock", "kio_dcpp");
    }

    void get(const std::string& file, int start) {
        writer->sendGet(file, start);
    }

    void onKeyReceived();

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

