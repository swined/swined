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
        Image resized = resize(blob, req.getParameter("w"), req.getParameter("h")); 
        if (resized != null) {
        	res.setContentType("image/" + resized.getFormat().name());
        	res.getOutputStream().write(resized.getImageData());
        } else
        	blobstoreService.serve(blob, res);
    }

	public BlobKey load(HttpServletRequest req) {
		String id = DomainUtils.guessSubdomain(req);
		String blobKey = ImageUtils.getBlobKey(id);
		if (blobKey == null)
			return null;
		else
			return new BlobKey(blobKey);
	}

	public Image resize(BlobKey blob, String sw, String sh) {
		int w = 0;
		if (sw != null)
			w = Integer.parseInt("0" + sw.replaceAll("[^0-9]", ""));
		int h = 0;		
		if (sh != null)
			h = Integer.parseInt("0" + sh.replaceAll("[^0-9]", ""));
    	if (w == 0 & h == 0)
    		return null;
    	else {
    		Image image = ImagesServiceFactory.makeImageFromBlob(blob);
    		Transform resize = ImagesServiceFactory.makeResize(w, h);
    		return imagesService.applyTransform(resize, image);
    	}
	}
	
}
