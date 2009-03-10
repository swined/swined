using System;
using System.Xml.Xsl;

class MainClass {
	public static int Main(string[] argv) {
		XslTransform xslt = new XslTransform();
		xslt.Load("style.xsl");
		xslt.Transform("src.xml", "dst.xml");
		return 1;
	}
}
