package controller;

import dao.JDBC.AnalyticsDaoJDBC;
import model.Analytics;
import model.LocationModel;
import org.json.simple.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The main controller of the microservice, handles all the routes the Server specifies.
 */
public class APIController {

    private String sessionId;
    private int webShopId = 1;
    private Timestamp startTime;
    private Timestamp endTime;
    private Date start;
    private Date stop;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Map<String, Integer> topLocations;


    public int getWebShopId() {
        return webShopId;
    }

    public String getSessionId() {
        return sessionId;
    }

    /**
     * The controller that's called from the root of the service.
     * @param req
     * @param res
     * @return ModelAndView that renders a blank html page which runs the JS to collect data.
     */
    public ModelAndView renderMain(Request req, Response res) {
        Map<Object, Object> params = new HashMap<>();
        startSession(req, res);
        return new ModelAndView(params, "time_location");
    }

    private void getTimes(Request req, Response res) throws ParseException {
        start = customDateParser(req.queryParams("startTime"));
        stop = customDateParser(req.queryParams("endTime"));
    }

    /**
     * Monitors the end of a session.
     * @param req
     * @param res
     * @return empty String
     */
    public String stopSession(Request req, Response res) {
        String time = req.queryParams("time");
        Date date = new Date(Long.parseLong(time));
        this.endTime = convertToTimeStamp(date);
        analytics(req, res);
        return "";
    }

    /**
     * Monitors the start of a new session.
     * @param req
     * @param res
     * @return empty String
     */
    public String startSession(Request req, Response res) {
        sessionId = req.session().id();
        Date date = new Date();
        this.startTime = convertToTimeStamp(date);
        return "";
    }

    /**
     * Creates a new record in the db with the data from the Request.
     * @param req
     * @param res
     */
    public void analytics(Request req, Response res) {
        LocationModel location = LocationModel.getAllLocations().get(0);
        float amount = 10;
        Currency currency = Currency.getInstance(Locale.US);
        Analytics model = new Analytics(getWebShopId(), getSessionId(), this.startTime, this.endTime, location, amount, String.valueOf(currency));
        try {
            new AnalyticsDaoJDBC().add(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all possible data of the service in one.
     * @param req
     * @param res
     * @return data about visitor count, average visit time, top location, and revenue in JSON format.
     * @throws ParseException when an error has been reached unexpectedly while parsing.
     */
    public String api(Request req, Response res) throws ParseException {
        webShopId = Integer.parseInt(req.queryParams("webshopId"));
        topLocations = LocationVisitorController.topLocations(Integer.parseInt(req.queryParams("webshopId")));
        int highestVisitorCount = Collections.max(topLocations.values());
        String topLocation = topLocations.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), highestVisitorCount))
                .map(Map.Entry::getKey).findFirst().orElse(null);
        Map<String, Object> analytic = new HashMap<>();
        analytic.put("visitors", VisitorCountController.totalVisitors(webShopId));
        analytic.put("average_visit_time", VisitTimeController.averageVisitTime(webShopId));
        analytic.put("most_visited_from", topLocation);
        analytic.put("revenue", RevenueController.totalRevenue(webShopId));
        return convertMapToJSONString(analytic);
    }

    /**
     * Gets visitor data based on the request parameters.
     * @param req
     * @param res
     * @return visitor count in JSON format.
     * @throws ParseException when an error has been reached unexpectedly while parsing.
     */
    public String visitorCounter(Request req, Response res) throws ParseException {
        webShopId = Integer.parseInt(req.queryParams("webshopId"));
        sessionId = req.queryParams("sessionId");
        Map<String, Integer> counter = new HashMap<>();
        if (req.queryParams().size() == 3) {
            getTimes(req, res);
            counter.put("visitors", VisitorCountController.visitorsByTime(webShopId, convertToTimeStamp(start), convertToTimeStamp(stop)));
        } else {
            counter.put("visitors", VisitorCountController.totalVisitors(webShopId));
        }
        return convertMapToJSONString(counter);
    }

    /**
     * Gets visit time data based on the request parameters.
     * @param req
     * @param res
     * @return visit time data in JSON format.
     * @throws ParseException when an error has been reached unexpectedly while parsing.
     */
    public String visitTimeCounter(Request req, Response res) throws ParseException {
        webShopId = Integer.parseInt(req.queryParams("webshopId"));
        sessionId = req.queryParams("sessionId");
        if (req.queryParams().size() == 3) {
            getTimes(req, res);
            return convertMapToJSONString(VisitTimeController.averageVisitTimeByTime(webShopId, convertToTimeStamp(start), convertToTimeStamp(stop)));
        } else {
            return convertMapToJSONString(VisitTimeController.averageVisitTime(webShopId));
        }
    }

    /**
     * Gets data on the locations of the visitors, based on the request parameters.
     * @param req
     * @param res
     * @return location(s) of visitors in JSON format.
     * @throws ParseException when an error has been reached unexpectedly while parsing.
     */
    public String locationVisits(Request req, Response res) throws ParseException {
        sessionId = req.queryParams("sessionId");
        webShopId = Integer.parseInt(req.queryParams("webshopId"));
        if (req.queryParams().size() == 3) {
            getTimes(req, res);
            return convertMapToJSONString(LocationVisitorController.topLocationsByTime(webShopId, convertToTimeStamp(start), convertToTimeStamp(stop)));
        } else return convertMapToJSONString(LocationVisitorController.topLocations(webShopId));
    }

    /**
     * @param req
     * @param res
     * @return revenue data in JSON format.
     * @throws ParseException when an error has been reached unexpectedly while parsing.
     */
    public String countRevenue(Request req, Response res) throws ParseException {
        sessionId = req.queryParams("sessionId");
        webShopId = Integer.parseInt(req.queryParams("webshopId"));
        Map<String, Float> revenue = new HashMap<>();
        if (req.queryParams().size() == 3) {
            getTimes(req, res);
            revenue.put("revenue", RevenueController.revenueByTime(webShopId, convertToTimeStamp(start), convertToTimeStamp(stop)));
        } else {
            revenue.put("revenue", RevenueController.totalRevenue(webShopId));
        }
        return convertMapToJSONString(revenue);
    }

    private Date customDateParser(String inputDate) throws ParseException {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date tempDate = inputFormat.parse(inputDate);
            String formattedDate = format.format(tempDate);
            return format.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Timestamp convertToTimeStamp(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.MILLISECOND, 0);
            return new java.sql.Timestamp(date.getTime());
        } else {
            return null;
        }
    }

    private String convertMapToJSONString(Map map){
        return (new JSONObject(){{putAll(map);}}).toString();
    }

}