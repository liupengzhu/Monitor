package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-22.
 */

public class WarningInfo {

    private List<DataBean> data;
    private String error;

    public String getError() {
        return error;
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
         * id : 462
         * company_name : 昆山广颖电线有限公司
         * type : 日常维护
         * content : 日常维护
         * status : 处理中
         * created_at : 2018-02-28 20:35:31
         */

        private int id;
        private String company_name;
        private String type;
        private String content;
        private String status;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
