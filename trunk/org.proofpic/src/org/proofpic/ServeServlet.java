package org.proofpic;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

@SuppressWarnings("serial")
public class ServeServlet extends HttpServlet {

	private final static BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private final static ImagesService imagesService = ImagesServiceFactory.getImagesService();

public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException {
        BlobKey blob = load(req);
		if (blob == null)
			res.sendRedirect("/404.jpg");
		Image image = ImagesServiceFactory.makeImageFromBlob(blob);
        Image resized = resize(image, req.getParameter("w"), req.getParameter("h"));
        serve(resized, res);
    }

	public BlobKey load(HttpServletRequest req) {
		String id = DomainUtils.guessSubdomain(req);
		return ImageUtils.getBlobKey(id);
	}

	private static void serve(Image image, HttpServletResponse res) throws IOException {
		BlobKey key = image.getBlobKey();
        if (key == null) {
        	res.setContentType("image/" + image.getFormat().name().toLowerCase());
        	res.getOutputStream().write(image.getImageData());
        } else
        	blobstoreService.serve(key, res);
	}
	
	private static Image resize(Image image, String sw, String sh) {
		int w = atoi(sw);
		int h = atoi(sh);		
    	if (w == 0 & h == 0)
    		return image;
    	else {
    		Transform resize = ImagesServiceFactory.makeResize(w, h);
    		return imagesService.applyTransform(resize, image);
    	}
	}
	
	private static int atoi(String a) {
		if (a == null)
			return 0;
		else
			return Integer.parseInt("0" + a.replaceAll("[^0-9]", ""));
	}
	
}
