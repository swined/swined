package nsuj083;

public class Main {

    public static void main(String[] args) {
        Server server = new Server();
        for (int i = 0; i < 4; i++)
            new Thread(new Client(server)).start();
        new Thread(server).start();
    }

}
