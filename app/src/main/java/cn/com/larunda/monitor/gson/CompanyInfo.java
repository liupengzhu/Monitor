package cn.com.larunda.monitor.gson;

import java.util.List;

import cn.com.larunda.monitor.bean.MaintenanceCompany;

/**
 * Created by sddt on 18-3-22.
 */

public class CompanyInfo {

    /**
     * current_page : 1
     * data : [{"company_id":"1","company_name":"昆山本广颖电线有限公司","company_industry":"钢铁业","company_tel":"0512-85425451","company_address":"张浦镇台玻西路388号","company_pic":"/api/file/image?/admin/company/20180313175447950603.jpg","maintenance_type":[{"name":"用电安全","icon":"power"},{"name":"空压机","icon":"air_compressor"}],"device_data":{"meter_total_num":17,"meter_is_malfunction":17,"meter_error":["配电站总表","100000884","光伏总表","总汽表","总瓦斯表","100000882","100000883","100000885","100000881","总水表","110000063","110000423","110000061","110000062","110000062","110000422","110000421"],"device_total":21,"error_total":17,"other_device":[{"name":"空调","data":{"total":4,"error":0,"error_name":[]}}]},"alarm_data":1,"maintenance_num":1},{"company_id":"2","company_name":"昆山荣欣旺机电有限公司","company_industry":"钢铁业","company_tel":"0512-85425451","company_address":"张浦镇清和路369号 ","company_pic":"/api/file/image?","maintenance_type":[{"name":"用电安全","icon":"power"},{"name":"空压机","icon":"air_compressor"}],"device_data":{"meter_total_num":1,"meter_is_malfunction":1,"meter_error":["配电站总表"],"device_total":1,"error_total":1,"other_device":[]},"alarm_data":1,"maintenance_num":1}]
     * from : 1
     * last_page : 1
     * next_page_url : null
     * path : http://ksdy_dev.dsmcase.com:90/api/integrated_maint_company/detail
     * per_page : 4
     * prev_page_url : null
     * to : 2
     * total : 2
     */

    private String error;
    private int current_page;
    private int from;
    private int last_page;
    private Object next_page_url;
    private String path;
    private int per_page;
    private Object prev_page_url;
    private int to;
    private int total;
    private List<DataBeanX> data;

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

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * company_id : 1
         * company_name : 昆山本广颖电线有限公司
         * company_industry : 钢铁业
         * company_tel : 0512-85425451
         * company_address : 张浦镇台玻西路388号
         * company_pic : /api/file/image?/admin/company/20180313175447950603.jpg
         * maintenance_type : [{"name":"用电安全","icon":"power"},{"name":"空压机","icon":"air_compressor"}]
         * device_data : {"meter_total_num":17,"meter_is_malfunction":17,"meter_error":["配电站总表","100000884","光伏总表","总汽表","总瓦斯表","100000882","100000883","100000885","100000881","总水表","110000063","110000423","110000061","110000062","110000062","110000422","110000421"],"device_total":21,"error_total":17,"other_device":[{"name":"空调","data":{"total":4,"error":0,"error_name":[]}}]}
         * alarm_data : 1
         * maintenance_num : 1
         */

        private String company_id;
        private String company_name;
        private String company_industry;
        private String company_tel;
        private String company_address;
        private String company_pic;
        private DeviceDataBean device_data;
        private int alarm_data;
        private int maintenance_num;
        private List<MaintenanceTypeBean> maintenance_type;
        private List<MaintenanceCompanyInfo.DataBean> maintenance_company;

        public List<MaintenanceCompanyInfo.DataBean> getMaintenanceCompanyInfoList() {
            return maintenance_company;
        }

        public void setMaintenanceCompanyInfoList(List<MaintenanceCompanyInfo.DataBean> maintenanceCompanyInfoList) {
            this.maintenance_company = maintenanceCompanyInfoList;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCompany_industry() {
            return company_industry;
        }

        public void setCompany_industry(String company_industry) {
            this.company_industry = company_industry;
        }

        public String getCompany_tel() {
            return company_tel;
        }

        public void setCompany_tel(String company_tel) {
            this.company_tel = company_tel;
        }

        public String getCompany_address() {
            return company_address;
        }

        public void setCompany_address(String company_address) {
            this.company_address = company_address;
        }

        public String getCompany_pic() {
            return company_pic;
        }

        public void setCompany_pic(String company_pic) {
            this.company_pic = company_pic;
        }

        public DeviceDataBean getDevice_data() {
            return device_data;
        }

        public void setDevice_data(DeviceDataBean device_data) {
            this.device_data = device_data;
        }

        public int getAlarm_data() {
            return alarm_data;
        }

        public void setAlarm_data(int alarm_data) {
            this.alarm_data = alarm_data;
        }

        public int getMaintenance_num() {
            return maintenance_num;
        }

        public void setMaintenance_num(int maintenance_num) {
            this.maintenance_num = maintenance_num;
        }

        public List<MaintenanceTypeBean> getMaintenance_type() {
            return maintenance_type;
        }

        public void setMaintenance_type(List<MaintenanceTypeBean> maintenance_type) {
            this.maintenance_type = maintenance_type;
        }

        public static class DeviceDataBean {
            /**
             * meter_total_num : 17
             * meter_is_malfunction : 17
             * meter_error : ["配电站总表","100000884","光伏总表","总汽表","总瓦斯表","100000882","100000883","100000885","100000881","总水表","110000063","110000423","110000061","110000062","110000062","110000422","110000421"]
             * device_total : 21
             * error_total : 17
             * other_device : [{"name":"空调","data":{"total":4,"error":0,"error_name":[]}}]
             */

            private int meter_total_num;
            private int meter_is_malfunction;
            private int device_total;
            private int error_total;
            private List<String> meter_error;
            private List<OtherDeviceBean> other_device;

            public int getMeter_total_num() {
                return meter_total_num;
            }

            public void setMeter_total_num(int meter_total_num) {
                this.meter_total_num = meter_total_num;
            }

            public int getMeter_is_malfunction() {
                return meter_is_malfunction;
            }

            public void setMeter_is_malfunction(int meter_is_malfunction) {
                this.meter_is_malfunction = meter_is_malfunction;
            }

            public int getDevice_total() {
                return device_total;
            }

            public void setDevice_total(int device_total) {
                this.device_total = device_total;
            }

            public int getError_total() {
                return error_total;
            }

            public void setError_total(int error_total) {
                this.error_total = error_total;
            }

            public List<String> getMeter_error() {
                return meter_error;
            }

            public void setMeter_error(List<String> meter_error) {
                this.meter_error = meter_error;
            }

            public List<OtherDeviceBean> getOther_device() {
                return other_device;
            }

            public void setOther_device(List<OtherDeviceBean> other_device) {
                this.other_device = other_device;
            }

            public static class OtherDeviceBean {
                /**
                 * name : 空调
                 * data : {"total":4,"error":0,"error_name":[]}
                 */

                private String name;
                private DataBean data;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public DataBean getData() {
                    return data;
                }

                public void setData(DataBean data) {
                    this.data = data;
                }

                public static class DataBean {
                    /**
                     * total : 4
                     * error : 0
                     * error_name : []
                     */

                    private int total;
                    private int error;
                    private List<?> error_name;

                    public int getTotal() {
                        return total;
                    }

                    public void setTotal(int total) {
                        this.total = total;
                    }

                    public int getError() {
                        return error;
                    }

                    public void setError(int error) {
                        this.error = error;
                    }

                    public List<?> getError_name() {
                        return error_name;
                    }

                    public void setError_name(List<?> error_name) {
                        this.error_name = error_name;
                    }
                }
            }
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
