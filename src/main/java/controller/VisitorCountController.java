package controller;


import dao.JDBC.AnalyticsDaoJDBC;

import java.sql.Timestamp;

/**
 * Fetches visitor count from the db.
 */
public class VisitorCountController {

    /**
     * Shows the total number of visitors.
     * @param webshop id of the webshop
     * @return total count of visitors for the requested webshop.
     */
    public static int totalVisitors(Integer webshop) {
        return new AnalyticsDaoJDBC().findByWebshop(webshop).size();
    }

    /**
     * Shows the total number of visitors, in the requested time frame.
     * @param webshop id of the webshop
     * @param startTime Timestamp of the start of the session
     * @param endTime Timestamp of the end of the session
     * @return total count of visitors for the requested webshop.
     */
    public static int visitorsByTime(Integer webshop, Timestamp startTime, Timestamp endTime) {
        return new AnalyticsDaoJDBC().findByWebshopTime(webshop, startTime, endTime).size();
    }
}
