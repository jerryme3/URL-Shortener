package jerme.url.shortener;

import jerme.url.shortener.server.ServerListenerThread;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        try {
            new ServerListenerThread().start();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
