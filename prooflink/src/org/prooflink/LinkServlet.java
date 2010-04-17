package org.prooflink;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LinkServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		String id = DomainUtils.guessSubdomain(req);
		try {
			Link link = Link.load(id);
			res.sendRedirect(link.getLink());
			PMUtils.save(new Request(link.getId(), req.getServerName()));
		} catch (Throwable e) {
			res.sendRedirect("/404.jsp?link=" + id);
		}
	}
}
