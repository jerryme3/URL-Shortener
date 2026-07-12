package jerme.url.shortener.http.parser;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FormStringParser {

    public static String getLongLink(String body) {
        Map<String, String> params = parse(body);
        return params.get("url");
    }

    private static Map<String, String> parse(String body) {
        Map<String, String> result = new HashMap<>();

        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                result.put(key, value);
            }
        }

        return result;
    }
}