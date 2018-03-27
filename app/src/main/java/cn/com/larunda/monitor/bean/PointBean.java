package cn.com.larunda.monitor.bean;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by sddt on 18-3-26.
 */

public class PointBean {
    private String name;
    private String rank;
    private LatLng latLng;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
