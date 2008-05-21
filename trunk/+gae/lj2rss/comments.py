from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch
import re

class CommentsPage(RequestHandler):
    def comments(self, user, itemid):
	res = fetch('http://m.lj.ru/read/user/%s/%s' % (user, itemid))
	if res.status_code != 200: 
	    return 'shit happened'
	rx = re.compile('<a href="/read/user/%s/%s/comments#comments">\S+? \((\d+)\)</a>' % (user, itemid))
	rm = rx.search(res.content)
	if rm: 
	    return rm.group(1) + 'comments'
	return 'no comments'
    def get(self):
	self.response.headers['Content-Type'] = 'text/css; charset=utf-8'
	u = self.request.get('user')
	i = self.request.get('itemid')
	c = 'shit happened'
	try:
	    c = self.comments(u, i)
	self.response.out.write('#cc_%s_%s:before { content: "%s" }' % (u, i, c))

def main(): CGIHandler().run(WSGIApplication([('/comments.info', CommentsPage)]))
if __name__ == '__main__': main()