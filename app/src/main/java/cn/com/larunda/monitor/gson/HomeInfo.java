package cn.com.larunda.monitor.gson;

/**
 * Created by sddt on 18-3-14.
 */

public class HomeInfo {

    /**
     * carbon : 3531.6
     * energy : 7220.8
     * gas : 1185.58
     * power : {"data":448.62,"ratio":"M"}
     * power_generated : {"data":63.11,"ratio":"M"}
     * safe_day : 10天
     * steam : 14597.71
     * water : 15764.69
     */

    private double carbon;
    private double energy;
    private double gas;
    private PowerBean power;
    private PowerGeneratedBean power_generated;
    private String safe_day;
    private double steam;
    private double water;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public double getCarbon() {
        return carbon;
    }

    public void setCarbon(double carbon) {
        this.carbon = carbon;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getGas() {
        return gas;
    }

    public void setGas(double gas) {
        this.gas = gas;
    }

    public PowerBean getPower() {
        return power;
    }

    public void setPower(PowerBean power) {
        this.power = power;
    }

    public PowerGeneratedBean getPower_generated() {
        return power_generated;
    }

    public void setPower_generated(PowerGeneratedBean power_generated) {
        this.power_generated = power_generated;
    }

    public String getSafe_day() {
        return safe_day;
    }

    public void setSafe_day(String safe_day) {
        this.safe_day = safe_day;
    }

    public double getSteam() {
        return steam;
    }

    public void setSteam(double steam) {
        this.steam = steam;
    }

    public double getWater() {
        return water;
    }

    public void setWater(double water) {
        this.water = water;
    }

    public static class PowerBean {
        /**
         * data : 448.62
         * ratio : M
         */

        private double data;
        private String ratio;

        public double getData() {
            return data;
        }

        public void setData(double data) {
            this.data = data;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }
    }

    public static class PowerGeneratedBean {
        /**
         * data : 63.11
         * ratio : M
         */

        private double data;
        private String ratio;

        public double getData() {
            return data;
        }

        public void setData(double data) {
            this.data = data;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }
    }
}
