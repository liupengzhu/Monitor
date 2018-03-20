package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-19.
 */

public class CarbonInfo {

    /**
     * chart : [300,317,314,311,315,319,304,303,313,319,305,305,318,320,302,319,301,303,303,null,null,null,null,null,null,null,null,null,null,null,null]
     * chart_type : bar
     * ratio :
     * table_data : [{"time":"03-01","data":300,"history_average":272,"range":10.29},{"time":"03-02","data":317,"history_average":252,"range":25.79},{"time":"03-03","data":314,"history_average":306,"range":2.61},{"time":"03-04","data":311,"history_average":302,"range":2.98},{"time":"03-05","data":315,"history_average":358,"range":-12.01},{"time":"03-06","data":319,"history_average":374,"range":-14.71},{"time":"03-07","data":304,"history_average":363,"range":-16.25},{"time":"03-08","data":303,"history_average":279,"range":8.6},{"time":"03-09","data":313,"history_average":323,"range":-3.1},{"time":"03-10","data":319,"history_average":400,"range":-20.25},{"time":"03-11","data":305,"history_average":310,"range":-1.61},{"time":"03-12","data":305,"history_average":362,"range":-15.75},{"time":"03-13","data":318,"history_average":384,"range":-17.19},{"time":"03-14","data":320,"history_average":263,"range":21.67},{"time":"03-15","data":302,"history_average":391,"range":-22.76},{"time":"03-16","data":319,"history_average":271,"range":17.71},{"time":"03-17","data":301,"history_average":366,"range":-17.76},{"time":"03-18","data":303,"history_average":367,"range":-17.44},{"time":"03-19","data":303,"history_average":358,"range":-15.36}]
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
         * data : 300
         * history_average : 272
         * range : 10.29
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
