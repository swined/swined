package nsuj083;

public class Main {

    public static void main(String[] args) {
        ServerThread<ISummator> server = new ServerThread(ISummator.class, new Summator());
        for (int i = 0; i < 4; i++)
            new Thread(new ClientThread(server.instance())).start();
        new Thread(server).start();
    }

}
