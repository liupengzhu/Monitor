package cn.com.larunda.monitor.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sddt on 18-3-21.
 */

public class MaintenanceCompany {
    private int id;
    private String name;
    private String tel;
    private String address;
    private String img;
    private String user;
    private String person;
    private String car;
    private String company;
    private List<String> typeList = new ArrayList<>();

    public MaintenanceCompany() {
        super();
    }

    public MaintenanceCompany(int id, String name, String tel, String address, String img, String user, String person, String car, String company, List<String> typeList) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.img = img;
        this.user = user;
        this.person = person;
        this.car = car;
        this.company = company;
        this.typeList = typeList;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }
}
