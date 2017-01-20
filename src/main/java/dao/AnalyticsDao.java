package dao;

import model.Analytics;

import java.util.List;
import java.sql.*;

/**
 * Defines general db operations for classes that implement it.
 */
public interface AnalyticsDao {

    /**
     * Saves new data to the db.
     * If a session is already stored, updates the values of that record.
     * @param model that stores all analytic data of a session
     */
    void add(Analytics model);

    /**
     * Finds all data of a webshop.
     * @param webshop id of the webshop
     * @return a List of Analytics of the requested webshop.
     */
    List<Analytics> findByWebshop(int webshop);

    /**
     * Finds all data of a webshop, in a requested time frame.
     * @param webshop id of the webshop
     * @param start Timestamp of the start of the session
     * @param end Timestamp of the end of the session
     * @return a List of Analytics of the requested webshop.
     */
    List<Analytics> findByWebshopTime(int webshop, Timestamp start, Timestamp end);

    /**
     * @param sessionId
     * @return a List of Analytics with the requested sessionId.
     */
    List<Analytics> findSessionId(String sessionId);
}
