package analyticsService.controller;

import analyticsService.SaltHasher;
import analyticsService.dao.JDBC.AnalyticsDaoJDBC;
import analyticsService.dao.JDBC.WebshopDaoJDBC;
import analyticsService.model.Analytics;
import analyticsService.model.LocationModel;
import analyticsService.model.Webshop;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import spark.Request;
import spark.Response;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by balazsando on 2017.01.16..
 */
public class TrackingController extends AbstractController{

    public Response stopSession(Request req, Response res) throws SQLException {
        String time = req.queryParams("time");
        Date date = new Date(Long.parseLong(time));
        new AnalyticsDaoJDBC().addData(new Analytics(null, req.session().id(), null, convertToTimeStamp(date), null));
        return res;
    }

    public Response startSession(Request req, Response res) throws SQLException {
        Webshop webshop = new WebshopDaoJDBC().findByApyKey(req.queryParams("apikey"));
        String time = req.queryParams("time");
        Date date = new Date(Long.parseLong(time));
        new AnalyticsDaoJDBC().addData(new Analytics(webshop, req.session().id(), convertToTimeStamp(date), convertToTimeStamp(date), null));
        return res;
    }

    public Response getData(Request req, Response res) throws org.json.simple.parser.ParseException, SQLException {
        JSONObject jsonLocation = (JSONObject) new JSONParser().parse(req.queryParams().iterator().next());
        LocationModel location = new LocationModel(jsonLocation.get("city").toString(),
                jsonLocation.get("country").toString(), jsonLocation.get("countryCode").toString());
        Webshop webshop = new WebshopDaoJDBC().findByApyKey(req.queryParams("apikey"));
        new AnalyticsDaoJDBC().addData(new Analytics(webshop, req.session().id(), null, null, location));
        return res;
    }

    public Response registerWebshop(Request req, Response res) throws UnsupportedEncodingException, SQLException, NoSuchAlgorithmException {
        new WebshopDaoJDBC().add(new Webshop(req.queryParams("ws_name")));
        res.redirect("/registered?apikey="+ SaltHasher.hashString(req.queryParams("ws_name")));
        return res;
    }
}
