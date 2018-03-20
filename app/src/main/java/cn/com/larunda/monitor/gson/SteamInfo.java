package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-19.
 */

public class SteamInfo {

    /**
     * chart : [320,308,314,304,300,301,305,319,309,316,316,309,315,313,308,315,302,309,303,null,null,null,null,null,null,null,null,null,null,null,null]
     * chart_type : bar
     * ratio :
     * table_data : [{"time":"03-01","data":320,"history_average":254,"range":25.98},{"time":"03-02","data":308,"history_average":255,"range":20.78},{"time":"03-03","data":314,"history_average":255,"range":23.14},{"time":"03-04","data":304,"history_average":280,"range":8.57},{"time":"03-05","data":300,"history_average":345,"range":-13.04},{"time":"03-06","data":301,"history_average":380,"range":-20.79},{"time":"03-07","data":305,"history_average":322,"range":-5.28},{"time":"03-08","data":319,"history_average":322,"range":-0.93},{"time":"03-09","data":309,"history_average":313,"range":-1.28},{"time":"03-10","data":316,"history_average":294,"range":7.48},{"time":"03-11","data":316,"history_average":376,"range":-15.96},{"time":"03-12","data":309,"history_average":369,"range":-16.26},{"time":"03-13","data":315,"history_average":345,"range":-8.7},{"time":"03-14","data":313,"history_average":255,"range":22.75},{"time":"03-15","data":308,"history_average":263,"range":17.11},{"time":"03-16","data":315,"history_average":309,"range":1.94},{"time":"03-17","data":302,"history_average":270,"range":11.85},{"time":"03-18","data":309,"history_average":370,"range":-16.49},{"time":"03-19","data":303,"history_average":349,"range":-13.18}]
     * x_name : ["03-01","03-02","03-03","03-04","03-05","03-06","03-07","03-08","03-09","03-10","03-11","03-12","03-13","03-14","03-15","03-16","03-17","03-18","03-19","03-20","03-21","03-22","03-23","03-24","03-25","03-26","03-27","03-28","03-29","03-30","03-31"]
     */

    private String chart_type;
    private String ratio;
    private List<String> chart;
    private List<TableDataBean> table_data;
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

    public List<String> getChart() {
        return chart;
    }

    public void setChart(List<String> chart) {
        this.chart = chart;
    }

    public List<TableDataBean> getTable_data() {
        return table_data;
    }

    public void setTable_data(List<TableDataBean> table_data) {
        this.table_data = table_data;
    }

    public List<String> getX_name() {
        return x_name;
    }

    public void setX_name(List<String> x_name) {
        this.x_name = x_name;
    }

    public static class TableDataBean {
        /**
         * time : 03-01
         * data : 320
         * history_average : 254
         * range : 25.98
         */

        private String time;
        private String data;
        private String history_average;
        private String range;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
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
    }
}
