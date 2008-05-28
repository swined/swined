from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch
from png import PNGCanvas
import re

font = None

class TextImage:
    fw = 12
    fh = 12
    ff = 'font.png'
    fs = '0123456789 abcdefghijklmnopqrstuvwxyz'
    rc = None
    def __init__(self, text):
	c = PNGCanvas(self.fw * (len(text) + 1), self.fh + 5)
	f = self.fc()
	for i in range(0, len(text)):
	    x = 2 + self.sc(text[i])
	    f.copyRect(x, 5, x + self.fw, 5 + self.fh, 5 + self.fw * i, 3, c)
	self.rc = c
    def fc(self):
	global font
	if not font: 
	    f = open(self.ff, 'rb')	
	    font = PNGCanvas(self.fw * len(self.fs), self.fh)
	    font.load(f)
	    f.close()
	return font
    def sc(self, s):
	r = self.fs.rfind(s)
	if r == -1: 
	    return self.fw * 10
	return self.fw * r
    def dump(self, stream):
	stream.write(self.rc.dump())

class CommentsPngPage(RequestHandler):
    def comments(self, user, itemid):
	res = None
	try: 
	    res = fetch('http://m.lj.ru/read/user/%s/%s' % (user, itemid))
	except: 
	    return 'shit happened'
	if res.status_code != 200: 
	    return 'error ' + str(res.status_code)
	rx = re.compile('<a href="/read/user/%s/%s/comments#comments">\S+? \((\d+)\)</a>' % (user, itemid))
	rm = rx.search(res.content)
	if rm: 
	    return rm.group(1) + ' comments'
	else: 	
	    return 'no comments'
    def get(self):
	self.response.headers['Content-Type'] = 'image/png'
	TextImage(self.comments(self.request.get('user'), self.request.get('itemid'))).dump(self.response.out)

def main(): 
    CGIHandler().run(WSGIApplication([('/comments.png', CommentsPngPage)]))

if __name__ == '__main__': 
    main()