package analyticsService.dao.JDBC;

import analyticsService.dao.AnalyticsDao;
import analyticsService.model.Analytics;
import analyticsService.model.Webshop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsDaoJDBC extends AbstractDaoJDBC implements AnalyticsDao {

    public void addData(Analytics analytics) throws SQLException {
        List<Analytics> match = findBySessionId(analytics.getSessionId());
        if (match.size() > 0){
            Analytics old = match.get(0);
            if (analytics.getEndTime() != null) old.setEndTime(analytics.getEndTime());
            if (analytics.getLocation() != null) old.setLocation(analytics.getLocation());
            if (analytics.getAmount() != null) old.setAmount(old.getAmount() + analytics.getAmount());
            if (analytics.getCurrency() != null) old.setCurrency(analytics.getCurrency());
            update(old);
        }
        else add(analytics);
    }

    private void add(Analytics analytics) throws SQLException {
        try (Connection connection = AbstractDaoJDBC.getConnection()) {
            String location = analytics.getLocation() != null ? analytics.getLocation().toString() : "N/A, N/A, N/A";
            Float amount = analytics.getAmount() != null ? analytics.getAmount() : 0f;
            PreparedStatement query;
            query = connection.prepareStatement("INSERT INTO webshopAnalytics (webshop_id," +
                    " session_id, visit_start, visit_end, location, amount, currency) VALUES (?, ?, ?, ?, ?, ?, ?);");
            query.setInt(1, analytics.getWebshop().getId());
            query.setString(2, analytics.getSessionId());
            query.setTimestamp(3, analytics.getStartTime());
            query.setTimestamp(4, analytics.getEndTime());
            query.setString(5, location);
            query.setFloat(6, amount);
            query.setString(7, String.valueOf(analytics.getCurrency()));
            query.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    private void update(Analytics analytics) throws SQLException {
        try (Connection connection = AbstractDaoJDBC.getConnection()) {
            PreparedStatement query = connection.prepareStatement("UPDATE webshopAnalytics SET visit_end = ?, location = ?, amount = ?, currency  = ? WHERE session_id = ?");
                query.setTimestamp(1, analytics.getEndTime());
                query.setString(2, analytics.getLocation().toString());
                query.setFloat(3, analytics.getAmount());
                query.setString(4, String.valueOf(analytics.getCurrency()));
                query.setString(5, analytics.getSessionId());
            query.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    public List<Analytics> findBySessionId(String sessionId) {
        return getAnalyticsList("SELECT * FROM webshopAnalytics " +
                "LEFT JOIN webshop ON webshop_id = ws_id " +
                "WHERE session_id ='" + sessionId + "';");
    }

    public List<Analytics> findByWebshop(String apiKey) {
        return getAnalyticsList("SELECT * FROM webshopAnalytics " +
                "LEFT JOIN webshop ON webshop_id = ws_id " +
                "WHERE webshop_id = (SELECT ws_id FROM webshop WHERE apikey ='" + apiKey + "');");
    }

    public List<Analytics> findByWebshopTime(String apiKey, Timestamp start, Timestamp end) {
        return getAnalyticsList("SELECT * FROM webshopAnalytics " +
                "LEFT JOIN webshop ON webshop_id = ws_id " +
                "WHERE webshop_id = (SELECT ws_id FROM webshop WHERE apikey ='" + apiKey + "') " +
                "AND visit_start >='" + start + "' " +
                "AND visit_end <='" + end + "';");
    }


    private List<Analytics> getAnalyticsList(String query) {
        List<Analytics> result = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query);
        ) {
            while (rs.next()) {
                Webshop webshop = new Webshop(rs.getString("ws_name"));
                webshop.setId(rs.getInt("ws_id"));
                Analytics analytics = new Analytics(
                        webshop,
                        rs.getString("session_id"),
                        rs.getTimestamp("visit_start"),
                        rs.getTimestamp("visit_end"),
                        stringToLocation(rs.getString("location")),
                        rs.getFloat("amount"),
                        rs.getString("currency"));
                analytics.setId(rs.getInt("an_id"));
                result.add(analytics);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
