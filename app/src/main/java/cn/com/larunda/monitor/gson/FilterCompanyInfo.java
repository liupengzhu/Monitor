package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-22.
 */

public class FilterCompanyInfo {

    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * name : 昆山广颖电线有限公司
         * num : {"total":"2","underway":"2"}
         */

        private int id;
        private String name;
        private NumBean num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public NumBean getNum() {
            return num;
        }

        public void setNum(NumBean num) {
            this.num = num;
        }

        public static class NumBean {
            /**
             * total : 2
             * underway : 2
             */

            private String total;
            private String underway;

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getUnderway() {
                return underway;
            }

            public void setUnderway(String underway) {
                this.underway = underway;
            }
        }
    }
}
