package analyticsService.model;

import java.sql.Timestamp;

public class Analytics {

    private Integer id;
    private Webshop webshop;
    private String sessionId;
    private Timestamp startTime;
    private Timestamp endTime;
    private LocationModel location;

    public Analytics(Webshop webshop, String sessionId, Timestamp startTime, Timestamp endTime, LocationModel location) {
        this.webshop = webshop;
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Webshop getWebshop() {
        return webshop;
    }

    public void setWebshop(Webshop webshop) {
        this.webshop = webshop;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public String toString() {
        return ": " + this.webshop + "\n" +
                "session: " + this.sessionId + "\n" +
                "start: " + this.startTime + "\n" +
                "end: " + this.endTime + "\n" +
                "location: " + this.location;
    }

    public Integer secondsSpent() {
        return Math.toIntExact((this.getEndTime().getTime()-this.getStartTime().getTime()) / 1000);
    }
}
