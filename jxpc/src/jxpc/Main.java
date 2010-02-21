package jxpc;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class Main {

    public static void main(String[] args) throws Exception {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9050));
        LJ lj = new LJ("swined", "", proxy);
        for (Object m : lj.getFriendsPage(0)) {
            System.out.println(m);
        }
    }
}
