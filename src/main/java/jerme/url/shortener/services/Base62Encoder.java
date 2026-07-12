package jerme.url.shortener.services;

public class Base62Encoder {
    private static final String ALPHABET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encode(long id) {
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            int remainder = (int) (id % 62);
            sb.append(ALPHABET.charAt(remainder));
            id /= 62;
        }
        return sb.reverse().toString();
    }
}