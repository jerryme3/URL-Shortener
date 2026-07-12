package jerme.url.shortener.services;

import java.util.Random;

public final class Utility {

    private static final Random RANDOM = new Random();

    public static long generateId() {
        return RANDOM.nextLong(1, Long.MAX_VALUE);
    }

}
