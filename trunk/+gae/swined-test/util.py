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
	if res.status_code == 200: 
	    return res.content
