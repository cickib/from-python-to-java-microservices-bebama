package connection.db;

import model.Analytics;

import java.sql.*;
import java.util.List;

/**
 * Created by makaimark on 2017.01.10..
 */
public class JDBCFunctions {

    public static void add(Analytics model) throws Exception {
        try (Connection connection = JDBCConnect.getConnection()) {
            PreparedStatement query = connection.prepareStatement("INSERT INTO webshopAnalytics (an_id, webshop_id," +
                    " session_id, visit_start, visit_end, location, amount, currency) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            query.setString(1, String.valueOf(model.getWebshopId()));
            query.setString(2, model.getSessionId());
            query.setString(3, String.valueOf(model.getStartTime()));
            query.setString(3, String.valueOf(model.getEndTime()));
            query.setString(3, String.valueOf(model.getAmount()));
            query.setString(3, String.valueOf(model.getCurrency()));
            query.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public List<Analytics> findByTime(int webshopI) {
//        Analytics result = null;
//        try (Connection connection = JDBCConnect.getConnection();
//             Statement statement = connection.createStatement();
//             ResultSet rs = statement.executeQuery("SELECT * FROM webshop WHERE u_id ='" + id + "';");
//        ) {
//            if (rs.next()) {
//                boolean emailStatus = rs.getInt("welcomeEmail") == 1;
//                result = new User(rs.getString("u_name"), rs.getString("email"), rs.getString("password"), emailStatus);
//                result.setId(id);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    public List<Analytics> findByWebshop()
}
