package org.example;

import java.io.IOException;
import javax.servlet.http.*;
import net.swined.LJ;

public class HelloAppEngineServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
                LJ lj = new LJ();
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
                resp.getWriter().println(lj.login("swined", "x"));
	}
}