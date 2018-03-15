package cn.com.larunda.monitor.bean;

/**
 * Created by sddt on 18-3-15.
 */

public class ElectricBean {
    private String time;                // 时间
    private String peak;                // 峰
    private String rush;                // 尖
    private String normal;
    private String valley;              // 谷
    private String total;               // 总
    private String history_average;     // 同期历史均值
    private String range;               // 增幅
    private String ratio;               // 单位进制

    public ElectricBean() {
        super();
    }

    public ElectricBean(String time, String peak, String rush, String normal, String valley, String total, String history_average, String range, String ratio) {
        this.time = time;
        this.peak = peak;
        this.rush = rush;
        this.normal = normal;
        this.valley = valley;
        this.total = total;
        this.history_average = history_average;
        this.range = range;
        this.ratio = ratio;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPeak() {
        return peak;
    }

    public void setPeak(String peak) {
        this.peak = peak;
    }

    public String getRush() {
        return rush;
    }

    public void setRush(String rush) {
        this.rush = rush;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getValley() {
        return valley;
    }

    public void setValley(String valley) {
        this.valley = valley;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getHistory_average() {
        return history_average;
    }

    public void setHistory_average(String history_average) {
        this.history_average = history_average;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
