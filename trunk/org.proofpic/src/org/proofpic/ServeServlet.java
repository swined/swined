package org.proofpic;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class ServeServlet extends HttpServlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException {
		String id = DomainUtils.guessSubdomain(req);
		Image img = Image.load(id);
		if (img == null)
			res.sendRedirect("/404.jpg");
        BlobKey blobKey = new BlobKey(img.getBlobKey());
        blobstoreService.serve(blobKey, res);
    }
}
