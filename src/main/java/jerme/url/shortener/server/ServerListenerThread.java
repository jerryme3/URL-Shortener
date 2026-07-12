package jerme.url.shortener.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {

    public static final int PORT = 8080;

    private final ServerSocket serverSocket;

    public ServerListenerThread() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    @Override
    public void run() {
        System.out.println("Server listening on port " + PORT);
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                System.out.println("Accepted connection from " + clientSocket.getInetAddress());

                new HttpConnectionWorkerThread(clientSocket).start();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}