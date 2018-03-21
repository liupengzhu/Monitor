package cn.com.larunda.monitor.gson;

import java.util.List;

/**
 * Created by sddt on 18-3-21.
 */

public class MaintenanceCompanyInfo {

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
         * id : 21
         * name : 理军物业
         * tel : 0519-87390102
         * address : 江苏省常州市溧阳市天目湖镇毛尖村C268西
         * log : /api/file/image?/admin/maintenance_company/20180309170254538995.png
         * connect_user : 张工
         * person : 1
         * car : 1
         * company : 2
         * maintenance_type : [{"name":"用电安全","icon":"power"},{"name":"空压机","icon":"air_compressor"},{"name":"空调","icon":"air_conditioner"},{"name":"水泵","icon":"pump"}]
         */

        private int id;
        private String name;
        private String tel;
        private String address;
        private String log;
        private String connect_user;
        private String person;
        private String car;
        private String company;
        private List<MaintenanceTypeBean> maintenance_type;

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

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLog() {
            return log;
        }

        public void setLog(String log) {
            this.log = log;
        }

        public String getConnect_user() {
            return connect_user;
        }

        public void setConnect_user(String connect_user) {
            this.connect_user = connect_user;
        }

        public String getPerson() {
            return person;
        }

        public void setPerson(String person) {
            this.person = person;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public List<MaintenanceTypeBean> getMaintenance_type() {
            return maintenance_type;
        }

        public void setMaintenance_type(List<MaintenanceTypeBean> maintenance_type) {
            this.maintenance_type = maintenance_type;
        }

        public static class MaintenanceTypeBean {
            /**
             * name : 用电安全
             * icon : power
             */

            private String name;
            private String icon;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}
