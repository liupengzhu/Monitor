package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-23.
 */

public class CompanyRankInfo {
    private String error;
    private List<String> data;
    private List<String> name;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
