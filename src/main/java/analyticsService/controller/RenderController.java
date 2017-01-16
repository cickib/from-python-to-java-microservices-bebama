package analyticsService.controller;

import analyticsService.dao.JDBC.WebshopDaoJDBC;
import spark.ModelAndView;
import spark.Request;
import spark.Response;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RenderController extends AbstractController{

    private DataController dataController = new DataController();

    public ModelAndView renderMain(Request req, Response res) throws SQLException {
        Map<Object, Object> params = new HashMap<>();
        params.put("webshops", new WebshopDaoJDBC().getAll());
        return new ModelAndView(params, "time_location");
    }

    public ModelAndView renderWebshop(Request req, Response res) throws SQLException {
        Map<Object, Object> params = new HashMap<>();
        try {
            dataController.checkParams(req);
        } catch (Exception e) {
            return new ModelAndView(new HashMap<String, String>() {{put("error", e.getMessage());}}, "error");
        }
        params.put("webshop", new WebshopDaoJDBC().findByApyKey(req.queryParams("apikey")));
        this.dataController.api(req, res);
        params.put("analytics", this.dataController.apiService.getParams());
        return new ModelAndView(params, "webshop_menu");
    }

    public ModelAndView renderRegister(Request req, Response res) throws SQLException {
        return new ModelAndView(new HashMap<>(), "register");
    }

    public ModelAndView renderRegistered(Request req, Response res) throws SQLException {
        Map<Object, Object> params = new HashMap<>();
        try {
            dataController.checkParams(req);
        } catch (Exception e) {
            return new ModelAndView(new HashMap<String, String>() {{put("error", e.getMessage());}}, "error");
        }
        params.put("webshop", new WebshopDaoJDBC().findByApyKey(req.queryParams("apikey")));
        return new ModelAndView(params, "registered");
    }
}
