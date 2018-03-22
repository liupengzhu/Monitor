package cn.com.larunda.monitor.bean;

/**
 * Created by sddt on 18-3-22.
 */

public class WarningBean {
    private String title;
    private String name;
    private String time;
    private String data;
    private String type;

    public WarningBean() {
        super();
    }

    public WarningBean(String title, String name, String time, String data, String type) {
        this.title = title;
        this.name = name;
        this.time = time;
        this.data = data;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
