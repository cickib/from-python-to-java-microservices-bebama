package dao.JDBC;

import model.LocationModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by cickib on 2017.01.09..
 */

/**
 * Defines db connection related methods for classes that extends it.
 */
public abstract class AbstractDaoJDBC {

    private static String DBURL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    /**
     * Gets the db connection to use with further db related actions.
     * @return the specific db connection
     * @throws SQLException when a database access error or other errors occur.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBURL, DB_USER, DB_PASSWORD);
    }

    /**
     * Sets up the connection with the db.
     * @param config connection data(dburl, user, password)
     * @throws IOException when failed or interrupted I/O operations occur.
     */
    public static void setConnection(String config) throws IOException {
        Properties pro = new Properties();
        FileInputStream in = new FileInputStream("./src/main/resources/" + config);
        pro.load(in);

        // getting values from property file
        DBURL = pro.getProperty("DBURL");
        DB_USER = pro.getProperty("DB_USER");
        DB_PASSWORD = pro.getProperty("DB_PASSWORD");
    }

    /**
     * A single String digested into a LocationModel to get useful location data about a visit.
     * @param location String from the Server
     * @return LocationModel instantiated with the data from the location String.
     */
    protected static LocationModel stringToLocation(String location) {
        String[] details = location.split(",");
        return new LocationModel(details[0],
                details[1].substring(1),
                details[2].substring(1));
    }

}
