package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-23.
 */

public class PowerUsageInfo {

    private List<String> x_name;
    private List<String> series;
    private String ratio;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getX_name() {
        return x_name;
    }

    public void setX_name(List<String> x_name) {
        this.x_name = x_name;
    }

    public List<String> getSeries() {
        return series;
    }

    public void setSeries(List<String> series) {
        this.series = series;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
