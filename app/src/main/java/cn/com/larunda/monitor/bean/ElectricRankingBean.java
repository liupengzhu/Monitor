package cn.com.larunda.monitor.bean;

/**
 * Created by sddt on 18-3-19.
 */

public class ElectricRankingBean {
    private String style;
    private String name;
    private String data;
    private String percent;
    private String ratio;

    public ElectricRankingBean() {
        super();
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
