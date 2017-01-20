package controller;

import model.LocationModel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import spark.Request;
import spark.Response;

/**
 * Created by cickib on 2017.01.09..
 */

/**
 * A controller that collects location data sent from JavaScript.
 */
public class LocationController {

    /**
     * Creates a new LocationModel object based on the Request data.
     * @param req
     * @param res
     * @return Response object
     * @throws ParseException when an error has been reached unexpectedly while parsing.
     */
    //   JSON keys: {city, country, countryCode}
    public static Response getData(Request req, Response res) throws ParseException {
        JSONObject jsonLocation = (JSONObject) new JSONParser().parse(req.queryParams().iterator().next());
        new LocationModel(jsonLocation.get("city").toString(),
                jsonLocation.get("country").toString(), jsonLocation.get("countryCode").toString());
        return res;
    }

}
