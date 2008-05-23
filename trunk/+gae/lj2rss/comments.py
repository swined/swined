from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch
import re
import png

class CommentsPngPage(RequestHandler):
    def get(self):
	self.response.headers['Content-Type'] = 'image/png'
	width, height = 150, 20
	c = PNGCanvas(width,height)
	c.color = [0xff,0,0,0xff]
	c.rectangle(0,0,width-1,height-1)
	c.verticalGradient(1,1,width-2, height-2,[0xff,0,0,0xff],[0x20,0,0xff,0x80])
        c.color = [0,0,0,0xff]
	c.line(0,0,width-1,height-1)
	c.line(0,0,width/2,height-1)
	c.line(0,0,width-1,height/2)
	c.copyRect(1,1,width/2-1,height/2-1,0,height/2,c)
	c.blendRect(1,1,width/2-1,height/2-1,width/2,0,c)
	self.response.out.write(c.dump())

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
    ]))
if __name__ == '__main__': main()