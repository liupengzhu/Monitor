package cn.com.larunda.monitor.bean;

/**
 * Created by sddt on 18-3-22.
 */

public class FilterCompanyBean {
    private int id;
    private String name;
    private String total;
    private String underway;
    private String type;

    public FilterCompanyBean() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUnderway() {
        return underway;
    }

    public void setUnderway(String underway) {
        this.underway = underway;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
