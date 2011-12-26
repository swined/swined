package org.swined.g2r;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AtomUtils {

	private AtomUtils() {}
	
	public static Document parse(InputStream in) throws ParserConfigurationException, SAXException, IOException {
	  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	  return dbf.newDocumentBuilder().parse(in);
	}
	
	public static Collection<Node> getEntries(Document doc) {
		return getChildNodesByTagName(doc.getNextSibling(), "entry");
	}
	
	private static Collection<Node> getChildNodesByTagName(Node node, String tag) {
		List<Node> nodes = new ArrayList<Node>();
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getLocalName().equals(tag))
				nodes.add(node);
		}
		return nodes;
	}
	
	private static Node getChildNodeByTagName(Node node, String tag) {
		Collection<Node> nodes = getChildNodesByTagName(node, tag);
		if (nodes.isEmpty())
			return null;
		else
			return nodes.iterator().next();
	}
	
	public static String getTitle(Node node) {
		return getChildNodeByTagName(node, "title").getTextContent();
	}
	
}
