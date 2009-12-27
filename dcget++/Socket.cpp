#include "Socket.h"
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <iostream>
#include "Exception.h"
#include <sstream>

Socket::Socket() : m_sock(-1) {
    memset(&m_addr, 0, sizeof ( m_addr));
}

Socket::~Socket() {
    if (is_valid())
        ::close(m_sock);
}

void Socket::create() {
    m_sock = socket(AF_INET, SOCK_STREAM, 0);
    if (!is_valid())
        throw Exception("invalid socket");
    int on = 1;
    if (setsockopt(m_sock, SOL_SOCKET, SO_REUSEADDR, (const char*) & on, sizeof ( on)) == -1)
        throw Exception("setsockopt() failed");
}

void Socket::send(const std::string s) const {
    int status = ::send(m_sock, s.c_str(), s.size(), MSG_NOSIGNAL);
    if (status == -1)
        throw Exception("send() failed");
}

void Socket::recv(std::string& s) const {
    char buf [ MAXRECV + 1 ];
    s = "";
    memset(buf, 0, MAXRECV + 1);
    errno = 0;
    int status = ::recv(m_sock, buf, MAXRECV, 0);
    int e;
    std::stringstream ss;
    switch (status) {
        case 0: return;
        case -1:
            e = errno;
            switch (e) {
                case EAGAIN:
                    return;
                case ENOMSG:
                    return;
                default:
                    ss << "socket error: " << strerror(e) << " (" << e << ")";
                    throw Exception(ss.str().c_str());
            };
        default:
//            ss << "received " << status << " bytes";
  //          throw Exception(ss.str().c_str());
            s = buf;
    }
}

void Socket::connect(const std::string host, const int port) {
    if (!is_valid())
        throw Exception("invalid socket");
    m_addr.sin_family = AF_INET;
    m_addr.sin_port = htons(port);
    int status = inet_pton(AF_INET, host.c_str(), &m_addr.sin_addr);
    if (errno == EAFNOSUPPORT)
        throw Exception("connection failed: EAFNOSUPPORT");
    status = ::connect(m_sock, (sockaddr *) & m_addr, sizeof ( m_addr));
    if (status != 0)
        throw Exception("connection failed");
}

void Socket::set_non_blocking(const bool b) {
    int opts;
    opts = fcntl(m_sock, F_GETFL);
    if (opts < 0) 
        throw Exception("fcntl() failed");
    if (b)
        opts = (opts | O_NONBLOCK);
    else
        opts = (opts & ~O_NONBLOCK);
    if (fcntl(m_sock, F_SETFL, opts) < 0)
        throw Exception("fcntl() failed");
}