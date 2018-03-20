package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-16.
 */

public class DayRenewableInfo {

    /**
     * chart : [293,284,253,269,264,277,289,281,297,277,293,254,281,292,282,253,264,297,291,292,285,260,279,270,279,292,271,261,250,288,276,293,272,280,261,286,256,300,267,252,277,260,257,258,252,289,261,266,286,252,258,270,262,288,290,292,280,261,252,280,299,279,272,270,258,283,256,265,283,274,267,260,284,275,268,287,263,280,252,300,283,261,270,295,299,259,286,278,271,289,258,269,268,280,289,276,263,296,291,297,269,258,257,253,283,275,290,297,255,293,296,288,253,265,283,252,275,269,280,296,258,288,265,276,268,254,252,282,300,293,278,269,251,285,272,285,260,261,281,266,253,277,254,257,292,287,259,267,256,290,262,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null]
     * mark_area : [[{"name":"峰","xAxis":"08:00","itemStyle":{"normal":{"color":"rgba(255,136,3,0.1)"}}},{"xAxis":"12:00"}],[{"name":"峰","xAxis":"14:00","itemStyle":{"normal":{"color":"rgba(255,136,3,0.1)"}}},{"xAxis":"15:00"}],[{"name":"峰","xAxis":"19:00","itemStyle":{"normal":{"color":"rgba(255,136,3,0.1)"}}},{"xAxis":"22:00"}],[{"name":"谷","xAxis":"00:00","itemStyle":{"normal":{"color":"rgba(95,183,96,0.1)"}}},{"xAxis":"08:00"}],[{"name":"平","xAxis":"12:00","itemStyle":{"normal":{"color":"rgba(70,140,200,0.1)"}}},{"xAxis":"14:00"}],[{"name":"平","xAxis":"15:00","itemStyle":{"normal":{"color":"rgba(70,140,200,0.1)"}}},{"xAxis":"19:00"}],[{"name":"平","xAxis":"22:00","itemStyle":{"normal":{"color":"rgba(70,140,200,0.1)"}}},{"xAxis":"23:55"}]]
     * chart_type : line
     * ratio : k
     * pie_ratio : k
     * chart_ratio : k
     * table_ratio : k
     * table_data : [{"time":"00:00-01:00","value":3331},{"time":"01:00-02:00","value":3346},{"time":"02:00-03:00","value":3309},{"time":"03:00-04:00","value":3195},{"time":"04:00-05:00","value":3271},{"time":"05:00-06:00","value":3266},{"time":"06:00-07:00","value":3318},{"time":"07:00-08:00","value":3322},{"time":"08:00-09:00","value":3329},{"time":"09:00-10:00","value":3305},{"time":"10:00-11:00","value":3283},{"time":"11:00-12:00","value":3202},{"time":"12:00-13:00","value":1913},{"time":"13:00-14:00","value":0},{"time":"14:00-15:00","value":0},{"time":"15:00-16:00","value":0},{"time":"16:00-17:00","value":0},{"time":"17:00-18:00","value":0},{"time":"18:00-19:00","value":0},{"time":"19:00-20:00","value":0},{"time":"20:00-21:00","value":0},{"time":"21:00-22:00","value":0},{"time":"22:00-23:00","value":0},{"time":"23:00-24:00","value":0}]
     * peak_valley_pie : [{"value":2284,"name":"峰  30.93%"},{"value":2554,"name":"谷  34.58%"},{"value":2547,"name":"平  34.49%"}]
     * legend : ["峰  30.93%","谷  34.58%","平  34.49%"]
     * x_name : ["00:00","00:05","00:10","00:15","00:20","00:25","00:30","00:35","00:40","00:45","00:50","00:55","01:00","01:05","01:10","01:15","01:20","01:25","01:30","01:35","01:40","01:45","01:50","01:55","02:00","02:05","02:10","02:15","02:20","02:25","02:30","02:35","02:40","02:45","02:50","02:55","03:00","03:05","03:10","03:15","03:20","03:25","03:30","03:35","03:40","03:45","03:50","03:55","04:00","04:05","04:10","04:15","04:20","04:25","04:30","04:35","04:40","04:45","04:50","04:55","05:00","05:05","05:10","05:15","05:20","05:25","05:30","05:35","05:40","05:45","05:50","05:55","06:00","06:05","06:10","06:15","06:20","06:25","06:30","06:35","06:40","06:45","06:50","06:55","07:00","07:05","07:10","07:15","07:20","07:25","07:30","07:35","07:40","07:45","07:50","07:55","08:00","08:05","08:10","08:15","08:20","08:25","08:30","08:35","08:40"]
     */

    private String chart_type;
    private String ratio;
    private String pie_ratio;
    private String chart_ratio;
    private String table_ratio;
    private List<String> chart;
    private List<List<MarkAreaBean>> mark_area;
    private List<TableDataBean> table_data;
    private List<PeakValleyPieBean> peak_valley_pie;
    private List<String> legend;
    private List<String> x_name;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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

    public List<String> getChart() {
        return chart;
    }

    public void setChart(List<String> chart) {
        this.chart = chart;
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

    public List<PeakValleyPieBean> getPeak_valley_pie() {
        return peak_valley_pie;
    }

    public void setPeak_valley_pie(List<PeakValleyPieBean> peak_valley_pie) {
        this.peak_valley_pie = peak_valley_pie;
    }

    public List<String> getLegend() {
        return legend;
    }

    public void setLegend(List<String> legend) {
        this.legend = legend;
    }

    public List<String> getX_name() {
        return x_name;
    }

    public void setX_name(List<String> x_name) {
        this.x_name = x_name;
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
         * time : 00:00-01:00
         * value : 3331
         */

        private String time;
        private String value;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class PeakValleyPieBean {
        /**
         * value : 2284
         * name : 峰  30.93%
         */

        private String value;
        private String name;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
