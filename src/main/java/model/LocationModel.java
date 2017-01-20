package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cickib on 2017.01.10..
 */

/**
 * Stores the location data in a more readable form.
 */
public class LocationModel {

    private String city;
    private String country;
    private String countryCode;

    /**
     * Class level variable to store instances.
     */
    private static List<LocationModel> allLocations = new ArrayList<>();

    /**
     * Saves every instance to the class level allLocations variable.
     * @param city
     * @param country
     * @param countryCode
     */
    public LocationModel(String city, String country, String countryCode) {
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
        addLocation(this);
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public static List<LocationModel> getAllLocations() {
        return allLocations;
    }

    private void addLocation(LocationModel location) {
        allLocations.add(location);
    }

    @Override
    public String toString(){
        return String.format("%s, %s, %s", getCity(), getCountry(), getCountryCode());
    }

}
