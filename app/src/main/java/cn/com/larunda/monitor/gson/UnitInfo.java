package cn.com.larunda.monitor.gson;

/**
 * Created by sddt on 18-3-14.
 */

public class UnitInfo {

    /**
     * elec : A
     * vol : V
     * load : kW
     * power : Wh
     * active_power : kWh
     * reactive_power : kvarh
     * installed_capacity : kVA
     * frequency : HZ
     * temperature : °C
     * water_usage : m³
     * gas_usage : m³
     * steam_usage : m³
     * carbon_emissions : tCO₂
     * energy_usage : tce
     * ten_million : 千万
     */

    private String elec;
    private String vol;
    private String load;
    private String power;
    private String active_power;
    private String reactive_power;
    private String installed_capacity;
    private String frequency;
    private String temperature;
    private String water_usage;
    private String gas_usage;
    private String steam_usage;
    private String carbon_emissions;
    private String energy_usage;
    private String ten_million;

    public String getElec() {
        return elec;
    }

    public void setElec(String elec) {
        this.elec = elec;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getActive_power() {
        return active_power;
    }

    public void setActive_power(String active_power) {
        this.active_power = active_power;
    }

    public String getReactive_power() {
        return reactive_power;
    }

    public void setReactive_power(String reactive_power) {
        this.reactive_power = reactive_power;
    }

    public String getInstalled_capacity() {
        return installed_capacity;
    }

    public void setInstalled_capacity(String installed_capacity) {
        this.installed_capacity = installed_capacity;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWater_usage() {
        return water_usage;
    }

    public void setWater_usage(String water_usage) {
        this.water_usage = water_usage;
    }

    public String getGas_usage() {
        return gas_usage;
    }

    public void setGas_usage(String gas_usage) {
        this.gas_usage = gas_usage;
    }

    public String getSteam_usage() {
        return steam_usage;
    }

    public void setSteam_usage(String steam_usage) {
        this.steam_usage = steam_usage;
    }

    public String getCarbon_emissions() {
        return carbon_emissions;
    }

    public void setCarbon_emissions(String carbon_emissions) {
        this.carbon_emissions = carbon_emissions;
    }

    public String getEnergy_usage() {
        return energy_usage;
    }

    public void setEnergy_usage(String energy_usage) {
        this.energy_usage = energy_usage;
    }

    public String getTen_million() {
        return ten_million;
    }

    public void setTen_million(String ten_million) {
        this.ten_million = ten_million;
    }
}
