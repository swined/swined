#include <stdio.h>
#include "oauth/src/oauth.h"

typedef struct Session {
	char *consumer_key;
	char *consumer_secret;
	char *access_token;
	char *access_secret;
} Session;

#include "session.inc"

int main(int argc, char **argv) {
	Session session;
	char *req_url = NULL;
	char *reply = NULL;

	initSession(&session);

	req_url = oauth_sign_url2(
		"https://api.twitter.com/1/statuses/home_timeline.rss", 
		NULL,
		OA_HMAC,
		NULL,
		session.consumer_key,
		session.consumer_secret,
		session.access_token,
		session.access_secret);
	printf("%s\n", req_url);	
	reply = oauth_http_get(req_url, NULL);
	if (reply) {
		printf("got reply\n");
		printf("%s\n", reply);
	}
	return 0;
}
