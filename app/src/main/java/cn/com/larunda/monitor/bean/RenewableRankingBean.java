package cn.com.larunda.monitor.bean;

/**
 * Created by sddt on 18-3-19.
 */

public class RenewableRankingBean {
    private String rank;
    private String name;
    private String total;
    private String history_average;     // 同期历史均值
    private String range;               // 增幅
    private String ratio;
    private String installedCapacityRatio;
    private String installedCapacity;

    public RenewableRankingBean() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getInstalledCapacityRatio() {
        return installedCapacityRatio;
    }

    public void setInstalledCapacityRatio(String installedCapacityRatio) {
        this.installedCapacityRatio = installedCapacityRatio;
    }

    public String getInstalledCapacity() {
        return installedCapacity;
    }

    public void setInstalledCapacity(String installedCapacity) {
        this.installedCapacity = installedCapacity;
    }
}
