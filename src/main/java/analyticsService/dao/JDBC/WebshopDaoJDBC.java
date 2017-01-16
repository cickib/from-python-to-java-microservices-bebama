package analyticsService.dao.JDBC;

import analyticsService.SaltHasher;
import analyticsService.dao.WebshopDao;
import analyticsService.model.Webshop;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WebshopDaoJDBC extends AbstractDaoJDBC implements WebshopDao {

    @Override
    public void add(Webshop webshop) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
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
    public Webshop findByApyKey(String apiKey) throws SQLException {
        Webshop result = null;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM webshop WHERE apikey ='" + apiKey + "';");
        ) {
            while (rs.next()) {
                result = new Webshop(rs.getString("ws_name"), rs.getString("apikey"));
                result.setId(rs.getInt("ws_id"));
                result.setAnalyticsList(new AnalyticsDaoJDBC().findByWebshop(apiKey));
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return result;
    }

    @Override
    public List<Webshop> getAll() throws SQLException {
        List<Webshop> result = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM webshop");
        ) {
            while (rs.next()) {
                Webshop webshop = new Webshop(rs.getString("ws_name"), rs.getString("apikey"));
                webshop.setId(rs.getInt("ws_id"));
                result.add(webshop);
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return result;
    }
}
