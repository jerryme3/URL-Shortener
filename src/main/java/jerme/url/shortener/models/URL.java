package jerme.url.shortener.models;

public record URL(
        long urlId,
        String origUrl,
        String shortUrl,
        long clickCount
) {}
