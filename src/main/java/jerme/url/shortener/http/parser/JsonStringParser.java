package jerme.url.shortener.http.parser;

import com.google.gson.JsonParser;

public class JsonStringParser {

    public static String getLongLink(String body) {

        //JsonObject
        var mapObj = JsonParser.parseString(body).getAsJsonObject();

        return mapObj.get("url").getAsString();
    }

}
