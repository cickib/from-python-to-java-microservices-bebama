package analyticsService;

import analyticsService.dao.JDBC.AbstractDaoJDBC;
import analyticsService.dao.JDBC.AnalyticsDaoJDBC;
import analyticsService.dao.JDBC.WebshopDaoJDBC;
import analyticsService.model.Analytics;
import analyticsService.model.LocationModel;
import analyticsService.model.Webshop;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class ExampleData {

    static List<Webshop> webshops = new ArrayList<>();
    static List<String> apiKeys = new ArrayList<>();
    static List<Analytics> analytics = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        populateData();
    }

    public static void populateData() throws Exception {
        AbstractDaoJDBC.setConnection("connection.properties");
        webshops();
        analytics();
    }

    public static void webshops() throws Exception {
        webshops.add(new Webshop("codecoolwebshop"));
        webshops.add(new Webshop("bebamashop"));
        webshops.add(new Webshop("maflashop"));
        webshops.add(new Webshop("oldschoolshop"));
        for (int i = 0; i < webshops.size(); i++) {
            new WebshopDaoJDBC().add(webshops.get(i));
            String apiKey = SaltHasher.hashString(webshops.get(i).getName());
            apiKeys.add(apiKey);
            System.out.println(new WebshopDaoJDBC().findByApyKey(apiKey));
            webshops.set(i, new WebshopDaoJDBC().findByApyKey(apiKey));
        }

    }

    public static void analytics() throws ParseException {
        LocationModel location0 = new LocationModel("Budapest", "Hungary", "HU");
        LocationModel location1 = new LocationModel("Miskolc", "Hungary", "HU");
        LocationModel location2 = new LocationModel("Kecskemet", "Hungary", "HU");
        LocationModel location3 = new LocationModel("Erd", "Hungary", "HU");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Timestamp start0 = new Timestamp(dateFormat.parse("2016-11-01 12:34:25").getTime());
        Timestamp start1 = new Timestamp(dateFormat.parse("2016-11-08 10:23:12").getTime());
        Timestamp start2 = new Timestamp(dateFormat.parse("2016-12-03 09:18:30").getTime());
        Timestamp start3 = new Timestamp(dateFormat.parse("2016-12-09 18:56:50").getTime());
        Timestamp start4 = new Timestamp(dateFormat.parse("2016-12-25 23:08:13").getTime());
        Timestamp start5 = new Timestamp(dateFormat.parse("2017-01-13 21:26:51").getTime());
        Timestamp end0 = new Timestamp(dateFormat.parse("2016-11-01 12:36:12").getTime());
        Timestamp end1 = new Timestamp(dateFormat.parse("2016-11-08 10:24:06").getTime());
        Timestamp end2 = new Timestamp(dateFormat.parse("2016-12-03 09:30:13").getTime());
        Timestamp end3 = new Timestamp(dateFormat.parse("2016-12-09 18:56:59").getTime());
        Timestamp end4 = new Timestamp(dateFormat.parse("2016-12-25 23:09:51").getTime());
        Timestamp end5 = new Timestamp(dateFormat.parse("2017-01-13 22:14:33").getTime());

        analytics.add(new Analytics(webshops.get(0), "efwefweefw", start0, end0, location0, 9990f, "HUF"));
        analytics.add(new Analytics(webshops.get(0), "ouifh23234", start1, end1, location0, 239900f, "HUF"));
        analytics.add(new Analytics(webshops.get(0), "qwdjqiwdjo", start2, end2, location1, 119900f, "HUF"));
        analytics.add(new Analytics(webshops.get(1), "ewhfioh23i", start3, end3, location0, 18990f, "HUF"));
        analytics.add(new Analytics(webshops.get(2), "efw2342345", start4, end4, location2, 5990f, "HUF"));
        analytics.add(new Analytics(webshops.get(3), "efwewef925", start5, end5, location3, 527990f, "HUF"));
        for (Analytics analytic : analytics) {
            new AnalyticsDaoJDBC().add(analytic);
        }
        for (String apiKey : apiKeys) {
            System.out.println(new AnalyticsDaoJDBC().findByWebshop(apiKey));
        }
    }
}
