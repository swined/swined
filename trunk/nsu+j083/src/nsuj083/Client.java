package nsuj083;

public class Client implements Runnable {

    private final Server server;
    private String response = null;

    public Client(Server server) {
        this.server = server;
    }

    public synchronized void run() {
        final String name = Thread.currentThread().getName();
        final Client client = this;
        while (true) {
            try {
                Thread.sleep(10000);
                server.run(new Runnable() {
                    public void run() {
                        System.out.println("[task] requestor = " + name + ", runner = " + Thread.currentThread().getName());
                        client.response(Thread.currentThread().getName());
                    }
                });
                wait();
                if (response != null)
                    System.out.println("[response] reciever = " + name + ", sender = " + response);
            } catch (Throwable e) {
                System.err.println("[cln] " + name + " : " + e);
            }
        }
    }

    private synchronized void response(String response) {
        this.response = response;
        notify();
    }

}
