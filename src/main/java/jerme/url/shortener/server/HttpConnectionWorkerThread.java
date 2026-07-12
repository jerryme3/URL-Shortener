package jerme.url.shortener.server;

import jerme.url.shortener.http.HttpRequest;
import jerme.url.shortener.http.HttpResponse;
import jerme.url.shortener.router.Router;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {

    private final Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                var in  = socket.getInputStream();
                var out = socket.getOutputStream()
            ) {

            var request  = HttpRequest.parse(in);
            var response = Router.route(request);
            response.writeTo(out);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            try {
                new HttpResponse(500, "Internal Server Error").writeTo(socket.getOutputStream());
            } catch (IOException ignored) {}
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
