package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-22.
 */

public class WorksheetInfo {
    /**
     * current_page : 1
     * data : [{"id":64,"company_name":"昆山本广颖电线有限公司","worksheet_number":"PL5934615762","status":"已取消","title":"日常维护  2018-03-06","created_at":"2018-03-06 17:32:05"},{"id":85,"company_name":"昆山本广颖电线有限公司","worksheet_number":"AL2778014579","status":"处理中","title":"负荷过载","created_at":"2018-03-07 17:54:32"},{"id":31,"company_name":"昆山本广颖电线有限公司","worksheet_number":"AL9064638311","status":"已完成","title":"负荷过载","created_at":"2018-02-28 15:05:57"},{"id":87,"company_name":"昆山荣欣旺机电有限公司","worksheet_number":"AL2855067680","status":"处理中","title":"导线高温","created_at":"2018-03-07 18:04:04"},{"id":44,"company_name":"昆山本广颖电线有限公司","worksheet_number":"PL2174471707","status":"处理中","title":"中央空调02需要更换滤网","created_at":"2018-03-01 19:59:38"},{"id":32,"company_name":"昆山本广颖电线有限公司","worksheet_number":"AL3304198876","status":"已完成","title":"漏电","created_at":"2018-02-28 20:31:43"},{"id":33,"company_name":"昆山本广颖电线有限公司","worksheet_number":"PL1335005570","status":"已完成","title":"日常维护  2018-02-28","created_at":"2018-02-28 20:35:31"},{"id":106,"company_name":"昆山本广颖电线有限公司","worksheet_number":"PL2794260190","status":"已完成","title":"中央空调04需要更换滤网","created_at":"2018-03-09 17:37:09"},{"id":86,"company_name":"昆山本广颖电线有限公司","worksheet_number":"PL3082482765","status":"处理中","title":"中央空调01需要更换滤网","created_at":"2018-03-07 17:55:49"},{"id":88,"company_name":"昆山本广颖电线有限公司","worksheet_number":"PL1978581208","status":"已完成","title":"日常维护  2018-03-07","created_at":"2018-03-07 18:31:18"}]
     * from : 1
     * last_page : 2
     * next_page_url : http://ksdy_dev.dsmcase.com:90/api/integrated_maint_company/worksheet_lists?page=2
     * path : http://ksdy_dev.dsmcase.com:90/api/integrated_maint_company/worksheet_lists
     * per_page : 10
     * prev_page_url : null
     * to : 10
     * total : 13
     */

    private int current_page;
    private int from;
    private int last_page;
    private String next_page_url;
    private String path;
    private int per_page;
    private Object prev_page_url;
    private int to;
    private int total;
    private String error;
    private List<DataBean> data;

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

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 64
         * company_name : 昆山本广颖电线有限公司
         * worksheet_number : PL5934615762
         * status : 已取消
         * title : 日常维护  2018-03-06
         * created_at : 2018-03-06 17:32:05
         */

        private int id;
        private String company_name;
        private String worksheet_number;
        private String status;
        private String title;
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

        public String getWorksheet_number() {
            return worksheet_number;
        }

        public void setWorksheet_number(String worksheet_number) {
            this.worksheet_number = worksheet_number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
