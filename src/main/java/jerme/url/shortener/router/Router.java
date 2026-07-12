package jerme.url.shortener.router;

import jerme.url.shortener.http.HttpRequest;
import jerme.url.shortener.http.HttpResponse;
import jerme.url.shortener.http.parser.FormStringParser;
import jerme.url.shortener.http.parser.JsonStringParser;
import jerme.url.shortener.models.URL;
import jerme.url.shortener.repository.URLRepository;
import jerme.url.shortener.services.Base62Encoder;
import jerme.url.shortener.services.FileCreator;
import jerme.url.shortener.services.Utility;

public class Router {

    private static final URLRepository URL_REPO = new URLRepository();

    public static HttpResponse route(HttpRequest request) {
        String method = request.getMethod();
        String path = request.getPath();

        if (method.equals("GET") && path.equals("/")) {
            return handleHome();
        }

        if (method.equals("POST") && path.equals("/shorten")) {
            return handleShorten(request);
        }

        if (method.equals("GET")) {
            return handleRedirect(path);
        }

        return new HttpResponse(404, "Not Found").body("Not Found");
    }

    private static HttpResponse handleHome() {
        String html = "<html>" +
                "<head><title>url shortener yarn</title></head>" +
                "<body>" +
                "<h1>URL Shortener</h1>" +
                "<form action=\"/shorten\" method=\"POST\">" +
                "<input type=\"text\" name=\"url\" placeholder=\"Paste a long URL\" size=\"50\">" +
                "<button type=\"submit\">Shorten</button>" +
                "</form>" +
                "</body>" +
                "</html>";

        return new HttpResponse(200, "OK")
                .contentType("text/html; charset=utf-8")
                .body(html);
    }

    private static HttpResponse handleShorten(HttpRequest request) {
        String contentType = request.getHeaders().get("Content-Type");
        String longLink;

        if (contentType != null && contentType.contains("application/json")) {
            longLink = JsonStringParser.getLongLink(request.getBody());
        } else {
            longLink = FormStringParser.getLongLink(request.getBody());
        }

        if (longLink == null || longLink.isBlank()) {
            return new HttpResponse(400, "Bad Request").body("Missing url parameter");
        }

        if (URL_REPO.existsByLongLink(longLink)) {
            return new HttpResponse(400, "Bad Request").body("The link for this URL already exists");
        }

        long id;

        do {
            id = Utility.generateId();
        } while (URL_REPO.existsById(id));

        String shortLink = Base62Encoder.encode(id);

        URL url = new URL(id, longLink, shortLink, 1);

        FileCreator.write(url.shortUrl());

        return URL_REPO.add(url) ? new HttpResponse(200, "OK")
                .contentType("application/json")
                .body("{\"shortCode\": \"" + shortLink + "\"}") :
                new HttpResponse(500, "Internal Server Error")
                        .body("Failed to save URL");
    }

    private static HttpResponse handleRedirect(String path) {
        String shortCode = path.substring(1); // strip leading "/"

        String longUrl = URL_REPO.findLongLink(shortCode);

        if (longUrl == null) {
            return new HttpResponse(404, "Not Found").body("Short URL not found");
        }

        URL_REPO.incrementClickCount(shortCode);

        return new HttpResponse(301, "Moved Permanently").location(longUrl);
    }
}