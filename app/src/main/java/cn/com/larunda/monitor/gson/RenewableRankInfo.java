package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-20.
 */

public class RenewableRankInfo {

    private List<DataBean> data;
    private String error;
    /**
     * current_page : 1
     * data : [{"rank":1,"company_name":"苏州金帝科机械设备有限公司","installed_capacity":"1002","total_generated":"6830.59","history_average":"6830.59","range":0},{"rank":2,"company_name":"昆山市康菲热流道系统有限公司","installed_capacity":"1009","total_generated":"4654.17","history_average":"4654.17","range":0},{"rank":3,"company_name":"昆山秧浦机械制造有限公司","installed_capacity":"1008","total_generated":"4538.58","history_average":"4538.58","range":0},{"rank":4,"company_name":"昆山衡泽盛精密设备有限公司","installed_capacity":"1003","total_generated":"4524.1","history_average":"4524.1","range":0},{"rank":5,"company_name":"昆山法欧特自动化设备有限公司","installed_capacity":"1005","total_generated":"4520.82","history_average":"4520.82","range":0},{"rank":6,"company_name":"昆山鼎宏五金精密模具有限公司","installed_capacity":"1001","total_generated":"4507.61","history_average":"4507.61","range":0},{"rank":7,"company_name":"昆山本广颖电线有限公司","installed_capacity":"1000","total_generated":"2285.24","history_average":"2285.24","range":0},{"rank":8,"company_name":"昆山荣欣旺机电有限公司","installed_capacity":"1010","total_generated":"2242.91","history_average":"2242.91","range":0},{"rank":9,"company_name":"昆山市港浦污水处理有限公司","installed_capacity":"1006","total_generated":"486.06","history_average":"486.06","range":0},{"rank":10,"company_name":"昆山市全九京金属热处理有限公司","installed_capacity":"1007","total_generated":"0","history_average":"0","range":"/"},{"rank":11,"company_name":"昆山市张浦花叶凹凸印刷厂","installed_capacity":"1004","total_generated":"0","history_average":"0","range":"/"}]
     * from : 1
     * last_page : 1
     * next_page_url : null
     * path : http://kunshan.dsmcase.com:90/api/renewable/rank_lists
     * per_page : 15
     * prev_page_url : null
     * to : 11
     * total : 11
     */

    private int current_page;
    private int from;
    private int last_page;
    private Object next_page_url;
    private String path;
    private int per_page;
    private Object prev_page_url;
    private int to;
    private int total;
    private String ratio;

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getError() {
        return error;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public Object getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(Object next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * rank : 1
         * company_name : 昆山市康菲热流道系统有限公司
         * installed_capacity : 1000
         * total_generated : 961
         * history_average : 794
         * range : 21.03
         */

        private String rank;
        private String company_name;
        private String installed_capacity;
        private String total_generated;
        private String history_average;
        private String range;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getInstalled_capacity() {
            return installed_capacity;
        }

        public void setInstalled_capacity(String installed_capacity) {
            this.installed_capacity = installed_capacity;
        }

        public String getTotal_generated() {
            return total_generated;
        }

        public void setTotal_generated(String total_generated) {
            this.total_generated = total_generated;
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
