package analyticsService.dao.JDBC;

import analyticsService.SaltHasher;
import analyticsService.dao.WebshopDao;
import analyticsService.model.Analytics;
import analyticsService.model.LocationModel;
import analyticsService.model.Webshop;

import java.sql.*;

public class WebshopDaoJDBC extends AbstractDaoJDBC implements WebshopDao {

    @Override
    public void add(Webshop webshop) throws Exception {
        try (Connection connection = AbstractDaoJDBC.getConnection()) {
            PreparedStatement query;
            query = connection.prepareStatement("INSERT INTO webshop (ws_name, apiKey) VALUES (?, ?);");
            query.setString(1, webshop.getName());
            query.setString(2, SaltHasher.hashString(webshop.getName()));
            query.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Webshop findByApyKey(String apiKey) {
        Webshop result = null;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM webshop WHERE apikey ='" + apiKey + "';");
        ) {
            while (rs.next()) {
                result = new Webshop(rs.getString("ws_name"));
                result.setId(rs.getInt("ws_id"));
                result.setAnalyticsList(new AnalyticsDaoJDBC().findByWebshop(rs.getString("apikey")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
