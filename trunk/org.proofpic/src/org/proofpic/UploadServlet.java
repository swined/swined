package org.proofpic;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {

	private static final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private static final UserService userService = UserServiceFactory.getUserService();

    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
		try {
			Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
			BlobKey blobKey = blobs.get("image");
			if (blobKey == null)
				res.sendRedirect("http://proofpic.org/");
			String key = ImageUtils.create(userService.getCurrentUser(), blobKey);
			res.sendRedirect("http://" + key + "." + DomainUtils.guessDomain(req) + "/");
		} catch (Throwable e) {
			res.setContentType("text/plain");
			e.printStackTrace(res.getWriter());
		}
    }
}
