package cn.com.larunda.monitor.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sddt on 18-3-21.
 */

public class Company {
    private int id;
    private String img;
    private String total;
    private float angle;

    private String name;

    private String industry;
    private String tel;
    private List<String> typeList = new ArrayList<>();
    private String address;
    private List<String> deviceList = new ArrayList<>();
    private String electric;
    private String alarm;
    private String maintenance;
    public Company() {
        super();
    }

    public Company(int id, String name, String industry, String tel, List<String> typeList, String address, List<String> deviceList, String alarm, String maintenance) {
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.tel = tel;
        this.typeList = typeList;
        this.address = address;
        this.deviceList = deviceList;
        this.alarm = alarm;
        this.maintenance = maintenance;
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<String> deviceList) {
        this.deviceList = deviceList;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    public String getElectric() {
        return electric;
    }

    public void setElectric(String electric) {
        this.electric = electric;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
