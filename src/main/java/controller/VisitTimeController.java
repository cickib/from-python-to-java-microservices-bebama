package controller;

import dao.JDBC.AnalyticsDaoJDBC;
import model.Analytics;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Gets stored visit time data of a webshop.
 */
public class VisitTimeController {

    /**
     * Shows the min, max, and average visit time a visitor spends in the webshop.
     * @param webshop id of the webshop
     * @return Map of min, max, and average visit time.
     */
    public static Map<String, String> averageVisitTime(Integer webshop) {
        return countAverage(new AnalyticsDaoJDBC().findByWebshop(webshop));
    }

    /**
     * Shows the min, max, and average visit time a visitor spends in the webshop, in the requested time frame.
     * @param webshop id of the webshop
     * @param startTime Timestamp of the start of the session
     * @param endTime Timestamp of the end of the session
     * @return Map of min, max, and average visit time.
     */
    public static Map<String, String> averageVisitTimeByTime(Integer webshop, Timestamp startTime, Timestamp endTime) {
        return countAverage(new AnalyticsDaoJDBC().findByWebshopTime(webshop, startTime, endTime));
    }

    private static Map<String, String> countAverage(List<Analytics> visits) {
        Map<String, String> statistics = new HashMap<String, String>(){{
            put("average", "00:00:00");
            put("min", "00:00:00");
            put("max", "00:00:00");
        }};
        if (visits.size() > 0) {
            statistics.put("average", intToString(getStream(visits).sum() / visits.size()));
            statistics.put("min", intToString(getStream(visits).min().getAsInt()));
            statistics.put("max", intToString(getStream(visits).max().getAsInt()));
        }
        return statistics;
    }

    private static IntStream getStream(List<Analytics> visits){
        return visits.stream().map(Analytics::secondsSpent).mapToInt(Integer::intValue);
    }

    private static String intToString(Integer duration) {
        Integer hours = duration / 3600;
        Integer minutes = (duration % 3600) / 60;
        Integer seconds = duration % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
