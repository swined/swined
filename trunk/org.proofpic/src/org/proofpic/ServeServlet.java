package org.proofpic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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

public void doGet(HttpServletRequest q, HttpServletResponse r)
    throws IOException {
		String k = DomainUtils.guessSubdomain(q);
        int w = atoi(q.getParameter("w"));
		int h = atoi(q.getParameter("h"));
		serve(resize(load(k), w, h), r);
    }

	private Image load(String key) throws IOException {
		BlobKey blobKey = ImageUtils.getBlobKey(key);
		if (blobKey == null)
			return loadStatic("/404.jpg");
		else 
			return ImagesServiceFactory.makeImageFromBlob(blobKey);
	}

	private Image loadStatic(String path) throws IOException {
		InputStream stream = null;
		try {
			stream = getServletContext().getResourceAsStream(path);
			if (stream == null)
				throw new IOException(path + " not found");
			byte[] r = new byte[0];
			byte[] b = new byte[1024];
			while (true) {
				int c = stream.read(b);
				if (c < 0)
					break;
				if (c == 0)
					continue;
				r = Arrays.copyOf(r, r.length + c);
				System.arraycopy(b, 0, r, r.length - c, c);
			}
			return ImagesServiceFactory.makeImage(r);
		} finally {
			if (stream != null)
				stream.close();
		}
	}
	
	private static void serve(Image image, HttpServletResponse res) throws IOException {
		BlobKey key = image.getBlobKey();
        if (key == null) {
        	res.setContentType("image/" + image.getFormat().name().toLowerCase());
        	res.getOutputStream().write(image.getImageData());
        } else
        	blobstoreService.serve(key, res);
	}
	
	private static Image resize(Image image, int w, int h) {
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
