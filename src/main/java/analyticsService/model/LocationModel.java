package analyticsService.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cickib on 2017.01.10..
 */

public class LocationModel {

    private String city;
    private String country;
    private String countryCode;

    public LocationModel(String city, String country, String countryCode) {
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
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

    @Override
    public String toString(){
        return String.format("%s, %s, %s", getCity(), getCountry(), getCountryCode());
    }

}
