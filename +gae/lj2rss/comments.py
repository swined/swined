from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch, GET, POST

class CommentsPage(RequestHandler):
    def ljsession(self, login, hash):
	data = 'mode=sessiongenerate&expiration=short&user=' + login + '&hpassword=' + hash
	res = fetch('http://www.livejournal.com/interface/flat', data, POST)
	if res.status_code != 200: return
	n = 0
	for ljsession in res.content.split("\n"):
	    if n: return ljsession
	    if ljsession == 'ljsession': n = 1
    def ljloggedin(self, ljsession):
	t = ljsession.split(':')
	return ':'.join([t[1], t[2]])
    def get(self):
	self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
	ljsession = self.ljsession(self.request.get('login'), self.request.get('hash'))
	if not ljsession: return self.response.out.write('shit happened')
	self.response.out.write(ljsession + '<br>' + self.ljloggedin(ljsession))

def main(): CGIHandler().run(WSGIApplication([('/comments.info', CommentsPage)], debug = True))
if __name__ == '__main__': main()