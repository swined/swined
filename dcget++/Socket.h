#ifndef _SOCKET_H
#define	_SOCKET_H

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <string>
#include <arpa/inet.h>
#include "Exception.h"

const int MAXRECV = 1024;

class Socket {
public:

    Socket() : m_sock(-1) {
    }

    Socket(const Socket& orig) : m_sock(orig.m_sock) {
    }
    virtual ~Socket();
    void create();
    void connect(const std::string host, const int port);
    void send(const std::string) const;
    void recv(std::string&) const;
    void set_non_blocking(const bool) const;

    bool is_valid() const {
        return m_sock != -1;
    }

private:

    int m_sock;

};

#endif	/* _SOCKET_H */

