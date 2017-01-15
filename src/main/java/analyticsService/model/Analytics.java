package analyticsService.model;

import java.sql.Timestamp;
import java.util.Currency;

public class Analytics {

    private Integer id;
    private Webshop webshop;
    private String sessionId;
    private Timestamp startTime;
    private Timestamp endTime;
    private LocationModel location;
    private Float amount;
    private Currency currency;

    public Analytics(Webshop webshop, String sessionId, Timestamp startTime, Timestamp endTime, LocationModel location, Float amount, String currency) {
        this.webshop = webshop;
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.amount = amount;
        this.currency = Currency.getInstance("HUF");
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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String toString() {
        return ": " + this.webshop + "\n" +
                "session: " + this.sessionId + "\n" +
                "start: " + this.startTime + "\n" +
                "end: " + this.endTime + "\n" +
                "location: " + this.location + "\n" +
                "amount: " + this.amount + "\n" +
                "currency: " + this.currency;
    }

    public Integer secondsSpent() {
        return Math.toIntExact((this.getEndTime().getTime()-this.getStartTime().getTime()) / 1000);
    }
}
