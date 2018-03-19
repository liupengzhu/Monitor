package cn.com.larunda.monitor.bean;

/**
 * Created by sddt on 18-3-19.
 */

public class GasBean {
    private String time;                // 时间
    private String total;
    private String history_average;     // 同期历史均值
    private String range;               // 增幅
    private String ratio;               // 单位进制

    public GasBean() {
        super();
    }

    public GasBean(String time, String total, String history_average, String range, String ratio) {
        this.time = time;
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
