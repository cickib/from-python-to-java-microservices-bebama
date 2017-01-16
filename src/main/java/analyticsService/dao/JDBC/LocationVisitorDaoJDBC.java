package analyticsService.dao.JDBC;

import analyticsService.dao.LocationVisitorDao;
import analyticsService.model.LocationVisitor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationVisitorDaoJDBC extends AbstractDaoJDBC implements LocationVisitorDao {

    public List<LocationVisitor> locationsByWebshop(String apiKey) throws SQLException {
        return getLocationList("SELECT count(*) AS totalVisitors, location FROM webshopAnalytics " +
                "WHERE webshop_id = (SELECT ws_id FROM webshop WHERE apikey ='" + apiKey + "') " +
                "GROUP BY location ORDER BY totalVisitors LIMIT 10;");
    }

    public List<LocationVisitor> locationsByWebshopTime(String apiKey, Timestamp start, Timestamp end) throws SQLException {
        return getLocationList("SELECT count(*) AS totalVisitors, location FROM webshopAnalytics " +
                " WHERE webshop_id = (SELECT ws_id FROM webshop WHERE apikey ='" + apiKey + "') " +
                "AND visit_start >='" + start + "' " +
                "AND visit_end <='" + end + "' GROUP BY location ORDER BY totalVisitors LIMIT 10;");
    }

    private List<LocationVisitor> getLocationList(String query) throws SQLException {
        List<LocationVisitor> result = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query);
        ) {
            while (rs.next()) {
                LocationVisitor location = new LocationVisitor(stringToLocation(rs.getString("location")),
                        rs.getInt("totalVisitors"));
                result.add(location);
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return result;
    }
}
