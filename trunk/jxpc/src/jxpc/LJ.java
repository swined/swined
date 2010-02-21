package jxpc;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransportFactory;

public class LJ {

    private static final String url = "http://livejournal.com/interface/xmlrpc";

    private final String login;
    private final String hpassword;
    private final Proxy proxy;

    public class LJEntry {

        public final String journalName;
        public final int logTime;
        public final int reply_count;
        public final String posterType;
        public final String posterUrl;
        public final String event;
        public final String journalType;
        public final String subject;
        public final String security;
        public final String userpic;
        public final String posterName;
        public final String journalUrl;
        public final int doCaptcha;
        public final int dItemId;

        public LJEntry(Map entry) {
            journalName = (String)entry.get("journalname");
            logTime = 0;
            reply_count = 0;
            posterType = "";
            posterUrl = "";
            event = "";
            journalType = "";
            subject = "";
            security = "";
            userpic = "";
            posterName = "";
            journalUrl = "";
            doCaptcha = 0;
            dItemId = 0;
        }

    }

    public LJ(String login, String hpassword, Proxy proxy) throws IllegalArgumentException {
        if (!hpassword.matches("^[A-Fa-f0-9]{32}$"))
            throw new IllegalArgumentException("invalid password format");
        this.login = login;
        this.hpassword = hpassword;
        this.proxy = proxy;
    }

    private void configureProxy(XmlRpcClient client) {
        XmlRpcSun15HttpTransportFactory factory = new XmlRpcSun15HttpTransportFactory(client);
        client.setTransportFactory(factory);
        if (proxy != null)
            factory.setProxy(proxy);
    }

    private XmlRpcClient getRpcClient() throws MalformedURLException {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(url));
        client.setConfig(config);
        configureProxy(client);
        return client;
    }

    public List<LJEntry> getFriendsPage(int skip) throws Exception {
        XmlRpcClient client = getRpcClient();
        Map<String, String> params = new HashMap();
        params.put("username", login);
        params.put("hpassword", hpassword);
        params.put("skip", Integer.toString(skip));
        Object result = client.execute("LJ.XMLRPC.getfriendspage", new Object[] { params });
        List<LJEntry> out = new ArrayList();
        for (Object o : (Object[])(((Map)result).get("entries")))
            out.add(new LJEntry((Map)o));
        return out;
    }

}
