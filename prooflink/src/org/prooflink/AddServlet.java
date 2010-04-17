package org.prooflink;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AddServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		try {
			UserService userService = UserServiceFactory.getUserService();
			Link link = Link.create(req.getParameter("url"), userService.getCurrentUser());
			res.sendRedirect("http://" + link.getKey() + "." + DomainUtils.guessDomain(req) + "/info.jsp");
		} catch (Throwable e) {
			res.setContentType("text/plain");
			e.printStackTrace(res.getWriter());
			//res.sendRedirect("http://" + DomainUtils.guessDomain(req) + "/");
		}
	}

}
