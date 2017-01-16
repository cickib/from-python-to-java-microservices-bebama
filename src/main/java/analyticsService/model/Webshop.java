package analyticsService.model;

import java.util.List;

public class Webshop {

    private Integer id;
    private String apikey;
    private String name;
    private List<Analytics> analyticsList;

    public Webshop(String name) {
        this.name = name;
    }

    public Webshop(String name, String apikey) {
        this.name = name;
        this.apikey = apikey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Analytics> getAnalyticsList() {
        return analyticsList;
    }

    public void setAnalyticsList(List<Analytics> analyticsList) {
        this.analyticsList = analyticsList;
    }

    public String toString(){
        return this.name;
    }
}
