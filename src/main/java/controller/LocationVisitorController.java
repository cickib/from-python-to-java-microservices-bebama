package controller;

import dao.JDBC.LocationVisitorDaoJDBC;
import model.LocationVisitor;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fetches top locations and visitor counts.
 */
public class LocationVisitorController {

    /**
     * Lists the locations from where the most visitors visited the site.
     * @param webshop id of the webshop
     * @return Map of location and visitor count.
     */
    public static Map<String, Integer> topLocations(Integer webshop) {
        return converter(new LocationVisitorDaoJDBC().locationsByWebshop(webshop));
    }

    /**
     * Lists the locations from where the most visitors visited the site, in the requested time frame.
     * @param webshop id of the webshop
     * @param startTime Timestamp of the start of the session
     * @param endTime Timestamp of the end of the session
     * @return Map of location and visitor count.
     */
    public static Map<String, Integer> topLocationsByTime(Integer webshop, Timestamp startTime, Timestamp endTime) {
        return converter(new LocationVisitorDaoJDBC().locationsByWebshopTime(webshop, startTime, endTime));
    }

    /**
     * Collects the visitor count for the location(s).
     * @param locations List of data of locations and visitors
     * @return Map of location and visitor count.
     */
    public static Map<String, Integer> converter(List<LocationVisitor> locations) {
        Map<String, Integer> locationMap = new HashMap<>();
        for (LocationVisitor location : locations) {
            locationMap.put(location.getLocation().toString(), location.getVisitors());
        }
        return locationMap;
    }
}
