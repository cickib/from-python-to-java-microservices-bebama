package analyticsService.controller;

import analyticsService.dao.JDBC.AnalyticsDaoJDBC;
import analyticsService.dao.JDBC.LocationVisitorDaoJDBC;
import analyticsService.dao.JDBC.WebshopDaoJDBC;
import analyticsService.model.Analytics;
import analyticsService.model.LocationVisitor;
import analyticsService.model.Webshop;
import analyticsService.service.APIService;
import spark.Request;
import spark.Response;

import java.security.InvalidParameterException;
import java.security.spec.InvalidParameterSpecException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class DataController extends AbstractController{

    protected APIService apiService;
    private Webshop webshop;
    private Date start = null;
    private Date end = null;

    public DataController(){
        this.apiService = new APIService();
    }

    public String api(Request req, Response res) throws SQLException {
        try {
            checkParams(req);
        } catch (Exception e) {
            return convertMapToJSONString(new HashMap<String, String>(){{put("error", e.getMessage());}});
        }
        this.apiService.visitorCount(collectData(req));
        this.apiService.visitTimeCount(collectData(req));
        this.apiService.mostVisitsFrom(collectLocation(req));
        return convertMapToJSONString(this.apiService.getParams());
    }

    public String visitorCount(Request req, Response res) throws SQLException {
        try {
            checkParams(req);
        } catch (Exception e) {
            return convertMapToJSONString(new HashMap<String, String>(){{put("error", e.getMessage());}});
        }
        this.apiService.visitorCount(collectData(req));
        return convertMapToJSONString(this.apiService.getParams());

    }

    public String visitTimeCount(Request req, Response res) throws SQLException {
        try {
            checkParams(req);
        } catch (Exception e) {
            return convertMapToJSONString(new HashMap<String, String>(){{put("error", e.getMessage());}});
        }
        this.apiService.visitTimeCount(collectData(req));
        return convertMapToJSONString(this.apiService.getParams());
    }

    public String locationVisits(Request req, Response res) throws SQLException {
        try {
            checkParams(req);
        } catch (Exception e) {
            return convertMapToJSONString(new HashMap<String, String>(){{put("error", e.getMessage());}});
        }
        this.apiService.locationVisits(collectLocation(req));
        return convertMapToJSONString(this.apiService.getParams());
    }

    protected void checkParams(Request req) throws InvalidParameterSpecException, SQLException {
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

    private List<Analytics> collectData(Request req) throws SQLException {
        if(this.start == null || this.end == null) {
            return new AnalyticsDaoJDBC().findByWebshop(req.queryParams("apikey"));
        } else {
            return new AnalyticsDaoJDBC().findByWebshopTime(
                    req.queryParams("apikey"),
                    convertToTimeStamp(this.start),
                    convertToTimeStamp(this.end));
        }
    }

    private List<LocationVisitor> collectLocation(Request req) throws SQLException {
        if(this.start == null || this.end == null) {
            return new LocationVisitorDaoJDBC().locationsByWebshop(req.queryParams("apikey"));
        } else {
            return new LocationVisitorDaoJDBC().locationsByWebshopTime(req.queryParams("apikey"),
                    convertToTimeStamp(this.start),
                    convertToTimeStamp(this.end));
        }
    }


}