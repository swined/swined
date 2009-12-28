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

const int MAXRECV = 100000;

class Socket {
public:

    Socket() : m_sock(socket(AF_INET, SOCK_STREAM, 0)) {
        if (!is_valid())
            throw Exception("create(): invalid socket");
    }

    Socket(const Socket& orig) : m_sock(orig.m_sock) {
        throw Exception("copying socket");
    }
    virtual ~Socket() {
        if (is_valid())
            ::close(m_sock);
    }
    void connect(const std::string host, const int port) const;
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

