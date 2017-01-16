package analyticsService.service;

import analyticsService.model.Analytics;
import analyticsService.model.LocationVisitor;

import java.util.*;
import java.util.stream.IntStream;

public class APIService {

    private Map<String, Object> params = new HashMap<>();

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(String webshop, Date start, Date end) {
        this.params.put("webshop", webshop);
        if (start != null) this.params.put("from", start);
        if (end != null) this.params.put("to", end);
    }

    public void visitorCount(List<Analytics> visitors){
        this.params.put("visitors", visitors.size());
    }

    public void locationVisits(List<LocationVisitor> locations) {
        Map<String, Integer> locationMap = new HashMap<>();
        for (LocationVisitor location : locations) {
            locationMap.put(location.getLocation().toString(), location.getVisitors());
        }
        this.params.put("locations", locationMap);
    }

    public void mostVisitsFrom(List<LocationVisitor> locations) {
        try {
            LocationVisitor location = Collections.max(locations, Comparator.comparing(LocationVisitor::getVisitors));
            this.params.put("most_visited_from", location.getLocation().toString());
        } catch (Exception e) {
            this.params.put("most_visited_from", "No visitors yet");
        }
    }

    public void visitTimeCount(List<Analytics> visits) {
        Map<String, String> times = new HashMap<>();
        times.put("average_time_spent", "00:00:00");
        times.put("min_time_spent", "00:00:00");
        times.put("max_time_spent", "00:00:00");
        if (visits.size() > 0) {
            times.put("average_time_spent", intToString(getStream(visits).sum() / visits.size()));
            times.put("min_time_spent", intToString(getStream(visits).min().getAsInt()));
            times.put("max_time_spent", intToString(getStream(visits).max().getAsInt()));
        }
        this.params.put("times", times);
    }

    private IntStream getStream(List<Analytics> visits){
        return visits.stream().map(Analytics::secondsSpent).mapToInt(Integer::intValue);
    }

    private String intToString(Integer duration) {
        Integer hours = duration / 3600;
        Integer minutes = (duration % 3600) / 60;
        Integer seconds = duration % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
