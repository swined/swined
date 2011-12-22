#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <sys/mount.h>
#include <dlfcn.h>
#include "inkview.h"
#include "oauth/src/oauth.h"

typedef struct Session {
	char *consumer_key;
	char *consumer_secret;
	char *access_token;
	char *access_secret;
} Session;

#include "session.inc"

char *getTimeline() {
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
	if (!req_url)
		return NULL;
	reply = oauth_http_get(req_url, NULL);
	free(req_url);
	return reply;
}

void timelineView() {

	char *timeline = getTimeline();

	if (!timeline)
		timeline = "shit happened";

	ClearScreen();


	DrawTextRect(5, 5, 190, 190, timeline, ALIGN_LEFT | VALIGN_TOP);

	FullUpdate();

}

int main_handler(int type, int par1, int par2) {

	if (type == EVT_SHOW) {

		timelineView();

	}

	if (type == EVT_KEYPRESS) {

		CloseApp();

	}

	return 0;

}

int main(int argc, char **argv) {

	InkViewMain(main_handler);
	return 0;
}
