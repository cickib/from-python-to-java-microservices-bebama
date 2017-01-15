package analyticsService.dao;

import analyticsService.model.Analytics;

import java.util.List;
import java.sql.*;

public interface AnalyticsDao {
    void addData(Analytics model) throws SQLException;
    List<Analytics> findBySessionId(String sessionId);
    List<Analytics> findByWebshop(String apiKey);
    List<Analytics> findByWebshopTime(String apiKey, Timestamp start, Timestamp end);
}
