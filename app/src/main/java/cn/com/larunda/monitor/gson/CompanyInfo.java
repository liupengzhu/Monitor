package cn.com.larunda.monitor.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sddt on 18-3-21.
 */

public class CompanyInfo {

    private String error;
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * company_id : 1
         * company_name : 昆山广颖电线有限公司
         * company_industry : 汽车制造业
         * company_tel : 0512-85425451
         * company_address : 张浦镇台玻西路388号
         * company_pic :
         * maintenance_type : [{"name":"空调","icon":"air_conditioner"},{"name":"空压机","icon":"air_compressor"},{"name":"监控","icon":"monitor"}]
         * device_data : {"meter_total_num":11,"meter_is_malfunction":2,"meter_error":["分表1-1功率因数过高","分表3-2漏电"],"device_total":14,"error_total":4,"other_device":{"空调":{"total":1,"error":1,"error_name":["中央空调01"]},"空压机":{"total":2,"error":1,"error_name":["空压机01"]},"监控":{"total":1,"error":0,"error_name":[]}}}
         * alarm_data : 8
         * maintenance_num : 5
         */

        private int company_id;
        private String company_name;
        private String company_industry;
        private String company_tel;
        private String company_address;
        private String company_pic;
        private DeviceDataBean device_data;
        private String alarm_data;
        private String maintenance_num;
        private List<MaintenanceTypeBean> maintenance_type;

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
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

        public String getAlarm_data() {
            return alarm_data;
        }

        public void setAlarm_data(String alarm_data) {
            this.alarm_data = alarm_data;
        }

        public String getMaintenance_num() {
            return maintenance_num;
        }

        public void setMaintenance_num(String maintenance_num) {
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
             * meter_total_num : 11
             * meter_is_malfunction : 2
             * meter_error : ["分表1-1功率因数过高","分表3-2漏电"]
             * device_total : 14
             * error_total : 4
             * other_device : {"空调":{"total":1,"error":1,"error_name":["中央空调01"]},"空压机":{"total":2,"error":1,"error_name":["空压机01"]},"监控":{"total":1,"error":0,"error_name":[]}}
             */

            private int meter_total_num;
            private int meter_is_malfunction;
            private int device_total;
            private int error_total;
            private OtherDeviceBean other_device;
            private List<String> meter_error;

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

            public OtherDeviceBean getOther_device() {
                return other_device;
            }

            public void setOther_device(OtherDeviceBean other_device) {
                this.other_device = other_device;
            }

            public List<String> getMeter_error() {
                return meter_error;
            }

            public void setMeter_error(List<String> meter_error) {
                this.meter_error = meter_error;
            }

            public static class OtherDeviceBean {
                /**
                 * 空调 : {"total":1,"error":1,"error_name":["中央空调01"]}
                 * 空压机 : {"total":2,"error":1,"error_name":["空压机01"]}
                 * 监控 : {"total":1,"error":0,"error_name":[]}
                 */

                @SerializedName("空调")
                private AirConditionerBean airConditioner;
                @SerializedName("空压机")
                private AirCompressorBean airCompressor;
                @SerializedName("监控")
                private MonitorBean monitor;
                @SerializedName("锅炉")
                private BoilerBean boiler;
                @SerializedName("水泵")
                private WaterPumpBean waterPump;
                @SerializedName("冰机")
                private IceMakerBean iceMaker;
                @SerializedName("风机")
                private FanBean fan;
                @SerializedName("照明")
                private LightingBean lighting;

                public AirConditionerBean getAirConditioner() {
                    return airConditioner;
                }

                public void setAirConditioner(AirConditionerBean airConditioner) {
                    this.airConditioner = airConditioner;
                }

                public AirCompressorBean getAirCompressor() {
                    return airCompressor;
                }

                public void setAirCompressor(AirCompressorBean airCompressor) {
                    this.airCompressor = airCompressor;
                }

                public MonitorBean getMonitor() {
                    return monitor;
                }

                public void setMonitor(MonitorBean monitor) {
                    this.monitor = monitor;
                }

                public BoilerBean getBoiler() {
                    return boiler;
                }

                public void setBoiler(BoilerBean boiler) {
                    this.boiler = boiler;
                }

                public WaterPumpBean getWaterPump() {
                    return waterPump;
                }

                public void setWaterPump(WaterPumpBean waterPump) {
                    this.waterPump = waterPump;
                }

                public IceMakerBean getIceMaker() {
                    return iceMaker;
                }

                public void setIceMaker(IceMakerBean iceMaker) {
                    this.iceMaker = iceMaker;
                }

                public FanBean getFan() {
                    return fan;
                }

                public void setFan(FanBean fan) {
                    this.fan = fan;
                }

                public LightingBean getLighting() {
                    return lighting;
                }

                public void setLighting(LightingBean lighting) {
                    this.lighting = lighting;
                }

                public static class AirConditionerBean {
                    /**
                     * total : 1
                     * error : 1
                     * error_name : ["中央空调01"]
                     */

                    private int total;
                    private int error;
                    private List<String> error_name;

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

                    public List<String> getError_name() {
                        return error_name;
                    }

                    public void setError_name(List<String> error_name) {
                        this.error_name = error_name;
                    }
                }

                public static class AirCompressorBean {
                    /**
                     * total : 2
                     * error : 1
                     * error_name : ["空压机01"]
                     */

                    private int total;
                    private int error;
                    private List<String> error_name;

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

                    public List<String> getError_name() {
                        return error_name;
                    }

                    public void setError_name(List<String> error_name) {
                        this.error_name = error_name;
                    }
                }

                public static class MonitorBean {
                    /**
                     * total : 1
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

                public static class BoilerBean {
                    /**
                     * total : 1
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

                public static class WaterPumpBean {
                    /**
                     * total : 1
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

                public static class IceMakerBean {
                    /**
                     * total : 1
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

                public static class FanBean {
                    /**
                     * total : 1
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

                public static class LightingBean {
                    /**
                     * total : 1
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
             * name : 空调
             * icon : air_conditioner
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
