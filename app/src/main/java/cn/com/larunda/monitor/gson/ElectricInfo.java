package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-16.
 */

public class ElectricInfo {

    /**
     * chart : {"normal":[4412,4154,4136,4409,4290,4383,4295,4239,4307,4327,4152,4225,4298,4195,4194,4147,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],"rush":[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],"valley":[4412,4154,4136,4409,4290,4383,4295,4239,4307,4327,4152,4225,4298,4195,4194,4147,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],"peak":[4412,4154,4136,4409,4290,4383,4295,4239,4307,4327,4152,4225,4298,4195,4194,4147,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null]}
     * mark_area : [[{"name":"峰","xAxis":"08:00","itemStyle":{"normal":{"color":"rgba(255,136,3,0.1)"}}},{"xAxis":"12:00"}],[{"name":"峰","xAxis":"14:00","itemStyle":{"normal":{"color":"rgba(255,136,3,0.1)"}}},{"xAxis":"15:00"}],[{"name":"峰","xAxis":"19:00","itemStyle":{"normal":{"color":"rgba(255,136,3,0.1)"}}},{"xAxis":"22:00"}],[{"name":"谷","xAxis":"00:00","itemStyle":{"normal":{"color":"rgba(95,183,96,0.1)"}}},{"xAxis":"08:00"}],[{"name":"平","xAxis":"12:00","itemStyle":{"normal":{"color":"rgba(70,140,200,0.1)"}}},{"xAxis":"14:00"}],[{"name":"平","xAxis":"15:00","itemStyle":{"normal":{"color":"rgba(70,140,200,0.1)"}}},{"xAxis":"19:00"}],[{"name":"平","xAxis":"22:00","itemStyle":{"normal":{"color":"rgba(70,140,200,0.1)"}}},{"xAxis":"23:55"}]]
     * chart_type : bar
     * ratio : k
     * pie_ratio : k
     * chart_ratio : k
     * table_ratio : k
     * table_data : [{"time":"2018-03-01","peak":4412,"rush":"/","valley":4412,"normal":4412,"total":13236,"history_average":14787,"range":-10.49},{"time":"2018-03-02","peak":4154,"rush":"/","valley":4154,"normal":4154,"total":12462,"history_average":13183,"range":-5.47},{"time":"2018-03-03","peak":4136,"rush":"/","valley":4136,"normal":4136,"total":12408,"history_average":11487,"range":8.02},{"time":"2018-03-04","peak":4409,"rush":"/","valley":4409,"normal":4409,"total":13227,"history_average":14131,"range":-6.4},{"time":"2018-03-05","peak":4290,"rush":"/","valley":4290,"normal":4290,"total":12870,"history_average":14605,"range":-11.88},{"time":"2018-03-06","peak":4383,"rush":"/","valley":4383,"normal":4383,"total":13149,"history_average":13925,"range":-5.57},{"time":"2018-03-07","peak":4295,"rush":"/","valley":4295,"normal":4295,"total":12885,"history_average":14844,"range":-13.2},{"time":"2018-03-08","peak":4239,"rush":"/","valley":4239,"normal":4239,"total":12717,"history_average":13431,"range":-5.32},{"time":"2018-03-09","peak":4307,"rush":"/","valley":4307,"normal":4307,"total":12921,"history_average":13876,"range":-6.88},{"time":"2018-03-10","peak":4327,"rush":"/","valley":4327,"normal":4327,"total":12981,"history_average":14487,"range":-10.4},{"time":"2018-03-11","peak":4152,"rush":"/","valley":4152,"normal":4152,"total":12456,"history_average":11749,"range":6.02},{"time":"2018-03-12","peak":4225,"rush":"/","valley":4225,"normal":4225,"total":12675,"history_average":12636,"range":0.31},{"time":"2018-03-13","peak":4298,"rush":"/","valley":4298,"normal":4298,"total":12894,"history_average":12575,"range":2.54},{"time":"2018-03-14","peak":4195,"rush":"/","valley":4195,"normal":4195,"total":12585,"history_average":12456,"range":1.04},{"time":"2018-03-15","peak":4194,"rush":"/","valley":4194,"normal":4194,"total":12582,"history_average":13311,"range":-5.48},{"time":"2018-03-16","peak":4147,"rush":"/","valley":4147,"normal":4147,"total":12441,"history_average":12072,"range":3.06}]
     * peak_valley_pie : []
     * legend : []
     * x_name : ["03-01","03-02","03-03","03-04","03-05","03-06","03-07","03-08","03-09","03-10","03-11","03-12","03-13","03-14","03-15","03-16","03-17","03-18","03-19","03-20","03-21","03-22","03-23","03-24","03-25","03-26","03-27","03-28","03-29","03-30","03-31"]
     */

    private ChartBean chart;
    private String chart_type;
    private String ratio;
    private String pie_ratio;
    private String chart_ratio;
    private String table_ratio;
    private List<List<MarkAreaBean>> mark_area;
    private List<TableDataBean> table_data;
    private List<?> peak_valley_pie;
    private List<?> legend;
    private List<String> x_name;

    public ChartBean getChart() {
        return chart;
    }

    public void setChart(ChartBean chart) {
        this.chart = chart;
    }

    public String getChart_type() {
        return chart_type;
    }

    public void setChart_type(String chart_type) {
        this.chart_type = chart_type;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getPie_ratio() {
        return pie_ratio;
    }

    public void setPie_ratio(String pie_ratio) {
        this.pie_ratio = pie_ratio;
    }

    public String getChart_ratio() {
        return chart_ratio;
    }

    public void setChart_ratio(String chart_ratio) {
        this.chart_ratio = chart_ratio;
    }

    public String getTable_ratio() {
        return table_ratio;
    }

    public void setTable_ratio(String table_ratio) {
        this.table_ratio = table_ratio;
    }

    public List<List<MarkAreaBean>> getMark_area() {
        return mark_area;
    }

    public void setMark_area(List<List<MarkAreaBean>> mark_area) {
        this.mark_area = mark_area;
    }

    public List<TableDataBean> getTable_data() {
        return table_data;
    }

    public void setTable_data(List<TableDataBean> table_data) {
        this.table_data = table_data;
    }

    public List<?> getPeak_valley_pie() {
        return peak_valley_pie;
    }

    public void setPeak_valley_pie(List<?> peak_valley_pie) {
        this.peak_valley_pie = peak_valley_pie;
    }

    public List<?> getLegend() {
        return legend;
    }

    public void setLegend(List<?> legend) {
        this.legend = legend;
    }

    public List<String> getX_name() {
        return x_name;
    }

    public void setX_name(List<String> x_name) {
        this.x_name = x_name;
    }

    public static class ChartBean {
        private List<Integer> normal;
        private List<Integer> rush;
        private List<Integer> valley;
        private List<Integer> peak;

        public List<Integer> getNormal() {
            return normal;
        }

        public void setNormal(List<Integer> normal) {
            this.normal = normal;
        }

        public List<Integer> getRush() {
            return rush;
        }

        public void setRush(List<Integer> rush) {
            this.rush = rush;
        }

        public List<Integer> getValley() {
            return valley;
        }

        public void setValley(List<Integer> valley) {
            this.valley = valley;
        }

        public List<Integer> getPeak() {
            return peak;
        }

        public void setPeak(List<Integer> peak) {
            this.peak = peak;
        }
    }

    public static class MarkAreaBean {
        /**
         * name : 峰
         * xAxis : 08:00
         * itemStyle : {"normal":{"color":"rgba(255,136,3,0.1)"}}
         */

        private String name;
        private String xAxis;
        private ItemStyleBean itemStyle;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getXAxis() {
            return xAxis;
        }

        public void setXAxis(String xAxis) {
            this.xAxis = xAxis;
        }

        public ItemStyleBean getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleBean itemStyle) {
            this.itemStyle = itemStyle;
        }

        public static class ItemStyleBean {
            /**
             * normal : {"color":"rgba(255,136,3,0.1)"}
             */

            private NormalBean normal;

            public NormalBean getNormal() {
                return normal;
            }

            public void setNormal(NormalBean normal) {
                this.normal = normal;
            }

            public static class NormalBean {
                /**
                 * color : rgba(255,136,3,0.1)
                 */

                private String color;

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }
            }
        }
    }

    public static class TableDataBean {
        /**
         * time : 2018-03-01
         * peak : 4412
         * rush : /
         * valley : 4412
         * normal : 4412
         * total : 13236
         * history_average : 14787
         * range : -10.49
         */

        private String time;
        private int peak;
        private String rush;
        private int valley;
        private int normal;
        private int total;
        private int history_average;
        private double range;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getPeak() {
            return peak;
        }

        public void setPeak(int peak) {
            this.peak = peak;
        }

        public String getRush() {
            return rush;
        }

        public void setRush(String rush) {
            this.rush = rush;
        }

        public int getValley() {
            return valley;
        }

        public void setValley(int valley) {
            this.valley = valley;
        }

        public int getNormal() {
            return normal;
        }

        public void setNormal(int normal) {
            this.normal = normal;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getHistory_average() {
            return history_average;
        }

        public void setHistory_average(int history_average) {
            this.history_average = history_average;
        }

        public double getRange() {
            return range;
        }

        public void setRange(double range) {
            this.range = range;
        }
    }
}
