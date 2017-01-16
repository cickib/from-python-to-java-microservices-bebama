package analyticsService.dao;

import analyticsService.model.Analytics;

import java.util.List;
import java.sql.*;

public interface AnalyticsDao {
    void addData(Analytics model) throws Exception;
    List<Analytics> findBySessionId(String sessionId) throws Exception;
    List<Analytics> findByWebshop(String apiKey) throws Exception;
    List<Analytics> findByWebshopTime(String apiKey, Timestamp start, Timestamp end) throws Exception;
}
