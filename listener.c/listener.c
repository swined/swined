#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>

int bind_to_port(int sock, int port) {
	struct sockaddr_in inaddr;
        memset(&inaddr, 0, sizeof(inaddr));
        inaddr.sin_family = AF_INET;
        inaddr.sin_addr.s_addr = htonl(INADDR_ANY);
        inaddr.sin_port = htons(port);
	return bind(sock, (struct sockaddr*)&inaddr, sizeof(inaddr));
}

int main(int argc, char **argv) {
	int sock, cli, t, i;
	socklen_t ss;
	char buf[1024];
	char greeting[] = "201 fake nntp server - no posting allowed\r\n\0";
	struct sockaddr_in cliaddr;
	if (-1 == (sock = socket(AF_INET, SOCK_STREAM, 0))) {
		printf("socket() failed: ");
		switch (errno) {
			case EACCES: printf("EACCES"); break;
			case EAFNOSUPPORT: printf(": EAFNOSUPPORT"); break;
			case EINVAL: printf("EINVAL"); break;
			case EMFILE: printf("EMFILE"); break;
			case ENFILE: printf("ENFILE"); break;
			case ENOBUFS: printf("ENOBUFS"); break;
			case ENOMEM: printf("ENOMEM"); break;
			case EPROTONOSUPPORT: printf("EPROTONOSUPPORT"); break;
			default: printf("%d", errno);
		}
		printf("\n");
		return -1;
	}
	if (-1 == bind_to_port(sock, 1119)) {
		printf("bind() failed: ");
		switch (errno) {
			case EADDRINUSE: printf("EADDRINUSE"); break;
			default: printf("%d", errno);
		}
		printf("\n");
		return -1;
	}
	if (-1 == listen(sock, 5)) {
		printf("listen() failed: ");
		switch (errno) {
			case EBADF: printf("EBADF"); break;
			default: printf("%d", errno);
		}
		printf("\n");
		return -1;
	}
	ss = sizeof(cliaddr);
	if (-1 == (cli = accept(sock, (struct sockaddr*)&cliaddr, &ss))) {
		printf("accept() failed: ");
		switch (errno) {
			case EAGAIN: printf("EAGAIN"); break;
			/* case EWOULDBLOCK: printf("EWOULDBLOCK"); break; */
			case EBADF: printf("EBADF"); break;
			case ECONNABORTED: printf("ECONNABORTED"); break;
			case EINTR: printf("EINTR"); break;
			case EINVAL: printf("EINVAL"); break;
			case EMFILE: printf("EMFILE"); break;
                        case ENFILE: printf("ENFILE"); break;
			case ENOTSOCK: printf("ENOTSOCK"); break;
			default: printf("%d", errno);
		}
		printf("\n");
		return -1;
	}
	printf("connection accepted\n");
	send(cli, &greeting, strlen(greeting), 0);
	while (0 != (t = recv(cli, &buf, 1024, 0))) {
		if (-1 == t) {
			printf("shit happened\n");
			break;
		}
		printf("%d bytes received\n", t);
		for (i = 0; i < t; i++)
			printf("%c", buf[i]);
		printf("\n");
	}
	return 0;
}
