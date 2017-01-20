package dao;

import model.LocationVisitor;

import java.util.List;
import java.sql.*;

/**
 * Defines location focused db operations for classes that implement it.
 */
public interface LocationVisitorDao {

    /**
     * Finds location data of a webshop.
     * @param webshop id of the webshop
     * @return a List of LocationVisitor objects of the requested webshop.
     */
    List<LocationVisitor> locationsByWebshop(int webshop);

    /**
     * Finds location data of a webshop, in a requested time frame.
     * @param webshop id of the webshop
     * @param start Timestamp of the start of the session
     * @param end Timestamp of the end of the session
     * @return a List of LocationVisitor objects of the requested webshop.
     */
    List<LocationVisitor> locationsByWebshopTime(int webshop, Timestamp start, Timestamp end);

}
