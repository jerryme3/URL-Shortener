package jerme.url.shortener.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private int statusCode;
    private String statusText;
    private String body = "";
    private String contentType = "text/plain; charset=utf-8";
    private String location = null; // for redirects

    public HttpResponse(int statusCode, String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public HttpResponse body(String body) {
        this.body = body;
        return this;
    }

    public HttpResponse contentType(String type) {
        this.contentType = type;
        return this;
    }

    public HttpResponse location(String location) {
        this.location = location;
        return this;
    }

    public void writeTo(OutputStream out) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusText).append("\r\n");
        sb.append("Content-Type: ").append(contentType).append("\r\n");
        sb.append("Content-Length: ").append(body.getBytes(StandardCharsets.UTF_8).length).append("\r\n");
        if (location != null) {
            sb.append("Location: ").append(location).append("\r\n");
        }
        sb.append("\r\n");
        sb.append(body);

        out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        out.flush();
    }
}