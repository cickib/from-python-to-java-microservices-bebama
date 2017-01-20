package model;

import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;

/**
 * Created by makaimark on 2017.01.11..
 */

/**
 * Creates a visual image of analytics data.
 */
public class Graph {
    public static final String API_URL = "http://chart.apis.google.com/chart?";

    /**
     * Creates a pie chart from the parameters.
     * @param size
     * @param name
     * @param values
     * @param partition
     * @param colors
     * @return url for the pie chart image.
     * @throws URISyntaxException when a String could not be parsed as a URI reference.
     */
    public String buildGraphURL(String size, String name, String values, String partition, String colors) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(API_URL);
        builder.addParameter("chs", size); // 200x400
        builder.addParameter("chdlp", "b"); // must have :D
        builder.addParameter("chtt", name);
        builder.addParameter("chdl", values); // needed format: Asleep|Awake|randomthing
        builder.addParameter("chd", "t:" + partition); // needed format: 1,89,10 ( 100 max)
        builder.addParameter("cht", "p"); // must have :D
        builder.addParameter("chco", colors); // needed format: 7D858F,586F8E,7D858F

        return String.valueOf(builder.build());
    }
}
