from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch
import re
from png import PNGCanvas

class TextImage:
    fw = 4
    fh = 10
    ff = 'font.png'
    fs = '0123456789 abcdefghijklmnopqrstuvwxyz'
    rc = None
    def __init__(self, text):
	c = PNGCanvas(self.fw * len(text), self.fh)
	f = self.fc()
	for i in range(1, len(text)):
	    x = self.sc(text[i])
	    f.copyRect(x, 0, x + self.fw, self.fh, 0, 0, c)
	self.rc = c
    def fc(self):
	f = open(self.ff, 'rb')
	c = PNGCanvas(self.fw * len(self.fs), self.fh)
	c.load(f)
	f.close()
	return c
    def sc(self, s):
	r = self.fs.rfind(s)
	if r == -1: return self.fw * 10
	return self.fw * (r - 1)
    def dump(self, stream):
	stream.write(self.rc.dump())

class CommentsPngPage(RequestHandler):
    def get(self):
	self.response.headers['Content-Type'] = 'image/png'
	TextImage('121221212212112').dump(self.response.out)

class CommentsSvgPage(RequestHandler):
    def comments(self, user, itemid):
	res = fetch('http://m.lj.ru/read/user/%s/%s' % (user, itemid))
	if res.status_code != 200: return ''
	rx = re.compile('<a href="/read/user/%s/%s/comments#comments">\S+? \((\d+)\)</a>' % (user, itemid))
	rm = rx.search(res.content)
	if rm: return rm.group(1) + 'comments'
	else: return 'no comments'
    def get(self):
	self.response.headers['Content-Type'] = 'image/svg+xml'
	u = self.request.get('user')
	i = self.request.get('itemid')
	c = 'unknown'
	try: c = self.comments(u, i)
	except Exception: c = ''
	self.response.out.write("""<?xml version="1.0" standalone="no"?>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
<svg width="150px" height="20px" version="1.1" xmlns="http://www.w3.org/2000/svg">
<text x="3" y="16" font-family="Verdana" font-size="16" fill="blue">%s</text>
</svg>""" % (c))

def main(): 
    CGIHandler().run(WSGIApplication([
	('/comments.svg', CommentsSvgPage),
	('/comments.png', CommentsPngPage),
    ], debug = True))
if __name__ == '__main__': main()