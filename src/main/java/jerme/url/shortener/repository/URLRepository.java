package jerme.url.shortener.repository;

import jerme.url.shortener.models.URL;

import java.sql.SQLException;

public class URLRepository {

    public boolean add(URL url) {
        var insert = "INSERT INTO url_shortener (url_id, orig_url, short_url, click_count) VALUES (?, ?, ?, ?)";

        try (var conn = DatabaseConnection.getConnection();
            var ps = conn.prepareStatement(insert)) {

            ps.setLong(1, url.urlId());
            ps.setString(2, url.origUrl());
            ps.setString(3, url.shortUrl());
            ps.setLong(4, url.clickCount());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public String findLongLink(String shortLink) {
        var findLongLink = "SELECT * FROM url_shortener WHERE short_url = ?";

        try (var conn = DatabaseConnection.getConnection();
            var ps = conn.prepareStatement(findLongLink)) {

            ps.setString(1, shortLink);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("orig_url");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public boolean existsByLongLink(String url) {
        var check = "SELECT 1 FROM url_shortener WHERE orig_url = ?";

        try (var conn = DatabaseConnection.getConnection();
            var ps = conn.prepareStatement(check)) {

            ps.setString(1, url);

            return ps.executeQuery().next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public void incrementClickCount(String shortLink) {
        var inc = "UPDATE url_shortener SET click_count = click_count + 1 WHERE short_url = ?";

        try (var conn = DatabaseConnection.getConnection();
            var ps = conn.prepareStatement(inc)) {

            ps.setString(1, shortLink);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public boolean existsById(long urlId) {
        var idSearch = "SELECT 1 FROM url_shortener WHERE url_id = ?";

        try (var conn = DatabaseConnection.getConnection();
            var ps = conn.prepareStatement(idSearch)) {

            ps.setLong(1, urlId);

            try (var rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
}
