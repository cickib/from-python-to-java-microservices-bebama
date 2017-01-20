package controller;

import dao.JDBC.AnalyticsDaoJDBC;
import model.Analytics;

import java.sql.Timestamp;
import java.util.List;

/**
 * Fetches revenue related data from the db.
 */
public class RevenueController {

    /**
     * Shows the total revenue of the webshop.
     * @param webshop id of the webshop
     * @return float amount of the revenue.
     */
    public static Float totalRevenue(Integer webshop) {
        return countRevenue(new AnalyticsDaoJDBC().findByWebshop(webshop));
    }

    /**
     * Shows the total revenue of the webshop, in the requested time frame.
     * @param webshop id of the webshop
     * @param startTime Timestamp of the start of the session
     * @param endTime Timestamp of the end of the session
     * @return float amount of the revenue.
     */
    public static Float revenueByTime(Integer webshop, Timestamp startTime, Timestamp endTime){
        return countRevenue(new AnalyticsDaoJDBC().findByWebshopTime(webshop, startTime, endTime));
    }

    private static Float countRevenue(List<Analytics> purchases){
        Double avenue = purchases.stream().map(Analytics::getAmount).mapToDouble(Float::floatValue).sum();
        return avenue.floatValue();
    }
}
