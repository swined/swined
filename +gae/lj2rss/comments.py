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
	c = 'unknown'
	try:
	    c = self.comments(u, i)
	except Exception:
	    c = 'shit happened'
	self.response.out.write('#cc_%s_%s:before { content: "%s" }' % (u, i, c))

class CommentsSvgPage(RequestHandler):
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
	self.response.headers['Content-Type'] = 'image/svg+xml'
	u = self.request.get('user')
	i = self.request.get('itemid')
	c = 'unknown'
	try:
	    c = self.comments(u, i)
	except Exception:
	    c = 'shit happened'
	    #width="150px" height="20px" 
	self.response.out.write(
"""<?xml version="1.0" standalone="no"?>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
<svg version="1.1" xmlns="http://www.w3.org/2000/svg">
<text x="3" y="16" font-family="Verdana" font-size="16" fill="blue">%s</text>
</svg>""" % (c))

def main(): CGIHandler().run(WSGIApplication([('/comments.info', CommentsPage), ('/comments.svg', CommentsSvgPage)]))
if __name__ == '__main__': main()