package jerme.url.shortener.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private String version;
    private Map<String, String> headers = new HashMap<>();
    private String body = "";

    public static HttpRequest parse(InputStream in) throws IOException {
        HttpRequest request = new HttpRequest();

        String requestLine = readLine(in);
        String[] parts = requestLine.split(" ");
        request.method = parts[0];
        request.path = parts[1];
        request.version = parts[2];

        String line;
        while (!(line = readLine(in)).isEmpty()) {
            int colonIndex = line.indexOf(":");
            request.headers.put(
                    line.substring(0, colonIndex).trim(),
                    line.substring(colonIndex + 1).trim()
            );
        }

        String contentLengthHeader = request.headers.get("Content-Length");
        if (contentLengthHeader != null) {
            int contentLength = Integer.parseInt(contentLengthHeader.trim());
            char[] bodyChars = new char[contentLength];
            for (int i = 0; i < contentLength; i++) {
                bodyChars[i] = (char) in.read();
            }
            request.body = new String(bodyChars);
        }

        return request;
    }

    private static String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int prev = -1, curr;
        while ((curr = in.read()) != -1) {
            if (prev == '\r' && curr == '\n') {
                sb.setLength(sb.length() - 1);
                break;
            }
            sb.append((char) curr);
            prev = curr;
        }
        return sb.toString();
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getVersion() { return version; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
}