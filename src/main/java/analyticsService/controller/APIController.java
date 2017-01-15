package analyticsService.controller;

import analyticsService.dao.JDBC.AnalyticsDaoJDBC;
import analyticsService.dao.JDBC.LocationVisitorDaoJDBC;
import analyticsService.dao.JDBC.WebshopDaoJDBC;
import analyticsService.model.Analytics;
import analyticsService.model.LocationModel;
import analyticsService.model.LocationVisitor;
import analyticsService.model.Webshop;
import analyticsService.service.APIService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.security.InvalidParameterException;
import java.security.spec.InvalidParameterSpecException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class APIController {

    private APIService apiService;
    private Webshop webshop;
    private Date start = null;
    private Date end = null;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public APIController(){
        this.apiService = new APIService();
    }

    public ModelAndView renderMain(Request req, Response res) {
        Map<Object, Object> params = new HashMap<>();
        return new ModelAndView(params, "time_location");
    }

    public Response stopSession(Request req, Response res) throws SQLException {
        String time = req.queryParams("time");
        Date date = new Date(Long.parseLong(time));
        new AnalyticsDaoJDBC().addData(new Analytics(null, req.session().id(), null, convertToTimeStamp(date), null, null, null));
        return res;
    }

    public Response startSession(Request req, Response res) throws SQLException {
        Webshop webshop = new WebshopDaoJDBC().findByApyKey(req.queryParams("apikey"));
        String time = req.queryParams("time");
        Date date = new Date(Long.parseLong(time));
        new AnalyticsDaoJDBC().addData(new Analytics(webshop, req.session().id(), convertToTimeStamp(date), convertToTimeStamp(date), null, null, null));
        return res;
    }

    public Response getData(Request req, Response res) throws org.json.simple.parser.ParseException, SQLException {
        JSONObject jsonLocation = (JSONObject) new JSONParser().parse(req.queryParams().iterator().next());
        LocationModel location = new LocationModel(jsonLocation.get("city").toString(),
                jsonLocation.get("country").toString(), jsonLocation.get("countryCode").toString());
        Webshop webshop = new WebshopDaoJDBC().findByApyKey(req.queryParams("apikey"));
        new AnalyticsDaoJDBC().addData(new Analytics(webshop, req.session().id(), null, null, location, null, null));
        return res;
    }

    public String api(Request req, Response res) {
        try {
            checkParams(req);
        } catch (Exception e) {
            return convertMapToJSONString(new HashMap<String, String>(){{put("error", e.getMessage());}});
        }
        this.apiService.visitorCount(collectData(req));
        this.apiService.visitTimeCount(collectData(req));
        this.apiService.mostVisitsFrom(collectLocation(req));
        this.apiService.revenueCount(collectData(req));
        return convertMapToJSONString(this.apiService.getParams());
    }

    public String visitorCount(Request req, Response res) {
        try {
            checkParams(req);
        } catch (Exception e) {
            return convertMapToJSONString(new HashMap<String, String>(){{put("error", e.getMessage());}});
        }
        this.apiService.visitorCount(collectData(req));
        return convertMapToJSONString(this.apiService.getParams());

    }

    public String visitTimeCount(Request req, Response res) {
        try {
            checkParams(req);
        } catch (Exception e) {
            return convertMapToJSONString(new HashMap<String, String>(){{put("error", e.getMessage());}});
        }
        this.apiService.visitTimeCount(collectData(req));
        return convertMapToJSONString(this.apiService.getParams());
    }

    public String locationVisits(Request req, Response res) {
        try {
            checkParams(req);
        } catch (Exception e) {
            return convertMapToJSONString(new HashMap<String, String>(){{put("error", e.getMessage());}});
        }
        this.apiService.locationVisits(collectLocation(req));
        return convertMapToJSONString(this.apiService.getParams());
    }

    public String revenueCount(Request req, Response res) {
        try {
            checkParams(req);
        } catch (Exception e) {
            return convertMapToJSONString(new HashMap<String, String>(){{put("error", e.getMessage());}});
        }
        this.apiService.revenueCount(collectData(req));
        return convertMapToJSONString(this.apiService.getParams());
    }

    private void checkParams(Request req) throws InvalidParameterSpecException {
        this.webshop = new WebshopDaoJDBC().findByApyKey(req.queryParams("apikey"));
        if (req.queryParams("apikey") == null) {
            throw new InvalidParameterSpecException("ApiKey is required");
        } else if (this.webshop == null) {
            throw new InvalidParameterException("Invalid apiKey");
        } else if (req.queryParams().size() == 3){
            if (req.queryParams("start") == null || req.queryParams("end") == null){
                throw new InvalidParameterSpecException("Invalid parameters");
            } else {
                try {
                    this.start = customDateParser(req.queryParams("start"));
                } catch (ParseException e) {
                    throw new InvalidParameterException("Invalid start parameter");
                }
                try {
                    this.end = customDateParser(req.queryParams("end"));
                } catch (ParseException e) {
                    throw new InvalidParameterException("Invalid end parameter");
                }
            }
        } else if (req.queryParams().size() != 1 && req.queryParams().size() != 3){
            throw new InvalidParameterSpecException("Invalid number of parameters");
        }
        this.apiService.setParams(this.webshop.getName(), this.start, this.end);
    }

    private List<Analytics> collectData(Request req) {
        if(this.start == null || this.end == null) {
            return new AnalyticsDaoJDBC().findByWebshop(req.queryParams("apikey"));
        } else {
            return new AnalyticsDaoJDBC().findByWebshopTime(
                    req.queryParams("apikey"),
                    convertToTimeStamp(this.start),
                    convertToTimeStamp(this.end));
        }
    }

    private List<LocationVisitor> collectLocation(Request req) {
        if(this.start == null || this.end == null) {
            return new LocationVisitorDaoJDBC().locationsByWebshop(req.queryParams("apikey"));
        } else {
            return new LocationVisitorDaoJDBC().locationsByWebshopTime(req.queryParams("apikey"),
                    convertToTimeStamp(this.start),
                    convertToTimeStamp(this.end));
        }
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