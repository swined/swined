from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.ext import db
from google.appengine.api.urlfetch import fetch
from png import PNGCanvas
import re

class Font:
    width = 0
    height = 0
    chars = ''
    canvas = None
    def __init__(self, width, height, file, chars):
	self.width = width
	self.height = height
	self.chars = chars
	self.canvas = PNGCanvas(0, 0)
	f = open(file, 'rb')	
	self.canvas.load(f)
	f.close()

font = Font(12, 12, 'font.png', '0123456789 abcdefghijklmnopqrstuvwxyz')
imc = {}

class TextImage:
    rc = None
    def __init__(self, text):
	global imc
	global font
	if imc.has_key(text):
	    self.rc = imc[text]
	else:
	    c = PNGCanvas(font.width * (len(text) + 1), font.height + 5)
	    for i in range(0, len(text)):
		x = 2 + self.sc(text[i])
	        font.canvas.copyRect(x, 5, x + font.width, 5 + font.height, 5 + font.width * i, 3, c)
	    imc[text] = c
	    self.rc = c
    def sc(self, s):
	global font
	r = font.chars.rfind(s)
	if r == -1: 
	    return font.width * 10
	return font.width * r
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
    CGIHandler().run(WSGIApplication([
	('/comments.png', CommentsPngPage),
    ], debug = True))

if __name__ == '__main__': 
    main()