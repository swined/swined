package org.proofpic;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class PicsJsonServlet extends HttpServlet {

	private static List<Entity> getImages(boolean all) {
		if (all) {
			return ImageUtils.getImages(null);
		} else {
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			return user == null ? new ArrayList<Entity>() : ImageUtils.getImages(user);
		}
	}
	
	public void doGet(HttpServletRequest q, HttpServletResponse r)
		    throws IOException {
				r.setContentType("application/json");
				PrintWriter w = r.getWriter();
				w.print("pics = [\n");
				for (Entity image : getImages(q.getAttribute("all") == null)) {
					w.print("{ id : \"" + image.getProperty("key") + "\", ");
					//w.print("url : \"" + ImageUtils.getServingUrl(image) + "\", ");
					Size size = ImageUtils.calcImageSize(image);
					if (size != null)
						w.print("w : " + size.width + ", h : " + size.height + ", ");
					w.print(" },\n");
				}
				w.print("];");
		    }

	
}
