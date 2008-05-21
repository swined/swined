from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch, GET, POST
from Cookie import SimpleCookie

class UserAgent:
    cookies = {}
    def cookieString(self): 
	return '; '.join(['%s=%s' % (k, v) for k, v in self.cookies.items()])
    def parseCookies(self, cookie):
	if cookie == '': return
	c = SimpleCookie()
	c.load(cookie)
	for k in c.keys(): 
	    self.cookies[k] = c.get(k).value
    def get(self, url): 
	return self.request(url)
    def post(self, url, data): 
	return self.request(url, data, POST)
    def request(self, url, data = None, method = GET):
	res = fetch(url, data, method, { 'Cookie' : self.cookieString() })
	if res.headers.has_key('Set-Cookie'): 
	    self.parseCookies(res.headers['Set-Cookie'])
	if res.status_code != 200: 
	    raise HttpError("http error " + str(res.status_code) + " (" + url + ")")
	return res.content

class HttpError(Exception): 
    pass

class CommentsPage(RequestHandler):
    ua = UserAgent
    def ljsession(self, login, hash):
	data = 'mode=sessiongenerate&expiration=short&user=' + login + '&hpassword=' + hash
	res = self.ua.post('http://www.livejournal.com/interface/flat', data)
	n = 0
	for ljsession in res.split("\n"):
	    if n: 
		return ljsession
	    if ljsession == 'ljsession': 
		n = 1
    def ljloggedin(self, ljsession):
	t = ljsession.split(':')
	return ':'.join([t[1], t[2]])
    def login(self, login, hash):
	ljsession = self.ljsession(login, hash)
	if not ljsession: 
	    return
	self.ua.cookies['ljsession'] = ljsession
	self.ua.cookies['ljloggedin'] = self.ljloggedin(ljsession)
	return True
    def get(self):
	self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
	if not self.login(self.request.get('login'), self.request.get('hash')):
	    return self.response.out.write('shit happened')
	self.response.out.write(self.ua.get(self.request.get('url') + "?format=light"))

def main(): CGIHandler().run(WSGIApplication([('/comments.info', CommentsPage)], debug = True))
if __name__ == '__main__': main()