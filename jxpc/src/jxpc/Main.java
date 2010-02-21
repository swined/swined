package jxpc;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransportFactory;

public class Main {

    public static void main(String[] args) throws Exception {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://livejournal.com/interface/xmlrpc"));
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcSun15HttpTransportFactory factory = new XmlRpcSun15HttpTransportFactory(client);
        factory.setProxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9050)));
        client.setTransportFactory(factory);
        client.setConfig(config);
        Map<String, String> params = new HashMap();
        params.put("username", "swined");
        params.put("hpassword", "");
        client.execute("LJ.XMLRPC.getfriendspage", new Object[] { params });
    }
}
