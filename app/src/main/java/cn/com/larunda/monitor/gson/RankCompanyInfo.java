package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-19.
 */

public class RankCompanyInfo {

    private ChartBean chart;
    private List<TableDataBean> table_data;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ChartBean getChart() {
        return chart;
    }

    public void setChart(ChartBean chart) {
        this.chart = chart;
    }

    public List<TableDataBean> getTable_data() {
        return table_data;
    }

    public void setTable_data(List<TableDataBean> table_data) {
        this.table_data = table_data;
    }

    public static class ChartBean {
        /**
         * data : [{"value":97.55,"name":"船舶业  13.9%"},{"value":91.55,"name":"电子信息产业  13.04%"},{"value":84.87,"name":"纺织业  12.09%"},{"value":77.84,"name":"钢铁业  11.09%"},{"value":73.99,"name":"汽车业  10.54%"},{"value":66.12,"name":"轻工业  9.42%"},{"value":60.52,"name":"物流业  8.62%"},{"value":56.39,"name":"有色金属业  8.03%"},{"value":49.01,"name":"装备制造业  6.98%"},{"value":44.08,"name":"石化产业  6.28%"}]
         * ratio : M
         * legend : ["船舶业  13.9%","电子信息产业  13.04%","纺织业  12.09%","钢铁业  11.09%","汽车业  10.54%","轻工业  9.42%","物流业  8.62%","有色金属业  8.03%","装备制造业  6.98%","石化产业  6.28%"]
         */

        private String ratio;
        private List<DataBean> data;
        private List<String> legend;

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public List<String> getLegend() {
            return legend;
        }

        public void setLegend(List<String> legend) {
            this.legend = legend;
        }

        public static class DataBean {
            /**
             * value : 97.55
             * name : 船舶业  13.9%
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

    public static class TableDataBean {
        /**
         * name : 船舶业
         * data : 97.55
         * percent : 13.9
         */

        private String name;
        private String data;
        private String percent;

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
    }
}
